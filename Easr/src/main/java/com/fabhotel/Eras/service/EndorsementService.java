package com.fabhotel.Eras.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import com.fabhotel.Eras.ExceptionHandler.RevieweeNotFoundException;
import com.fabhotel.Eras.ExceptionHandler.ReviewerNotFoundException;
import com.fabhotel.Eras.model.Endorsement;
import com.fabhotel.Eras.model.User;
import com.fabhotel.Eras.repository.EndorsementRepository;
import com.fabhotel.Eras.repository.SkillRepository;
import com.fabhotel.Eras.repository.UserRepository;
import com.fabhotel.Eras.util.Constants;
import com.fabhotel.Eras.util.EnumChecker;
import com.fabhotel.Eras.util.SkillHierarchyUtil;
import com.fabhotel.Eras.util.TadjustedScore;
import com.fabhotel.Eras.util.TechSkill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EndorsementService {
    private static final Logger logger = LoggerFactory.getLogger(EndorsementService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SkillRepository skillRepository;
    
    @Autowired
    private EndorsementRepository endorsementRepository;

    @Transactional
    public Endorsement postEndorsement(@NotBlank String revieweeUserId, @NotBlank String reviewerUserId,
                                       @NotBlank String skillName, @NotBlank double score) {
        logger.debug("Fetching reviewee with ID: {}", revieweeUserId);
        User reviewee =userRepository.findById(revieweeUserId).
        		orElseThrow(()->new RevieweeNotFoundException("Reviewee not found with ID: "+revieweeUserId));
        logger.debug("Fetching reviewer with ID: {}", reviewerUserId);
        User reviewer = userRepository.findById(reviewerUserId).
        		orElseThrow(()->new ReviewerNotFoundException("Reviewer not found with ID: "+reviewerUserId));
        logger.debug("Fetching skill with name: {}", skillName);
        logger.debug("Fetching skill with name: {}", skillName);
        TechSkill reviewerSkill = getTechSkill(reviewer.getSkill());
        TechSkill revieweeSkill = getTechSkill(reviewee.getSkill());
        double skillScore = getSkillScore(reviewerSkill, revieweeSkill);
        TadjustedScore adjustedScore = calculateAdjustedScore(reviewer, reviewee, skillName, skillScore);

        Endorsement endorsement = new Endorsement();
        endorsement.setAdjustedScore(adjustedScore.getAdjustedScore());
        endorsement.setComment(adjustedScore.getComment());
        endorsement.setScore(score);
        endorsement.setReviewee(reviewee);
        endorsement.setReviewer(reviewer);
        endorsement.setSkillName(skillName);

        logger.debug("Saving endorsement");
        return endorsementRepository.save(endorsement);
    }

    public static int getSkillScore(TechSkill endorserSkill, TechSkill endorsedSkill) {
        if (endorserSkill == endorsedSkill) {
            return Constants.EXACT_SKILL_MATCH;
        } else if (SkillHierarchyUtil.isBroaderSkill(endorserSkill, endorsedSkill)) {
            return Constants.BRODER_SKILL_MATCH;
        } else if (SkillHierarchyUtil.isBroaderSkill(endorsedSkill, endorserSkill)) {
            return Constants.SUB_SKILL_SAME_BRODER_MATCH;
        } else {
            return Constants.SKILL_MISMATCH;
        }
    }

   
    public Map<String, List<String>> getEndorsements(String userId) {
        List<Endorsement> endorsements = endorsementRepository.findByRevieweeUserId(userId);

        return endorsements.stream()
                .collect(Collectors.groupingBy(Endorsement::getSkillName,
                        Collectors.mapping(this::formatEndorsement, Collectors.toList())));
    }

    private String formatEndorsement(Endorsement endorsement) {
        return String.format("%s - %f (%.1f with %s)",
                endorsement.getReviewer().getUserId(),
                endorsement.getScore(),
                endorsement.getAdjustedScore(),
                endorsement.getComment());
    }

    public static TadjustedScore calculateAdjustedScore(User reviewer, User reviewee, String skill, double skillScore) {
        double baseScore = Constants.BASE_SCORE;
        double adjustedScore = baseScore + skillScore;
        StringBuilder comment = new StringBuilder("Base score: " + baseScore);
        
        appendBonus(adjustedScore, comment, areCoworkers(reviewee, reviewer), Constants.COWORKER_BONUS, Constants.ADDFORCOWORKTEXT, Constants.NOTACOWORKER);
        appendFieldBonus(adjustedScore, comment, areSameField(reviewee, reviewer), checkExpYear(reviewee, reviewer), Constants.SAME_FIELD_BONUS, Constants.SAMEFIELDBONUSTEXT, Constants.EXPERIENCE_BONUS, Constants.ADDEXPBONUSTEXT);

        return new TadjustedScore(adjustedScore, comment.toString());
    }

    private static void appendBonus(double adjustedScore, StringBuilder comment, boolean condition, double bonus, Object addforcoworktext, Object notacoworker) {
        if (condition) {
            adjustedScore += bonus;
            comment.append(", +").append(bonus).append(addforcoworktext);
        } else {
            comment.append(notacoworker);
        }
    }
    private TechSkill getTechSkill(String skill) {
        try {
        	
        	return EnumChecker.getEnumFromString(TechSkill.class, skill, TechSkill.SKILLNOTMATCH);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid skill: " + skill, e);
        }
    }
    private static void appendFieldBonus(double adjustedScore, StringBuilder comment, boolean isSameField, boolean checkExpYear, double fieldBonus, Object samefieldbonustext, double expBonus, Object addexpbonustext) {
        if (isSameField) {
            adjustedScore += fieldBonus;
            comment.append(", +").append(fieldBonus).append(samefieldbonustext);
            if (checkExpYear) {
                adjustedScore += expBonus;
                comment.append(", +").append(expBonus).append(addexpbonustext);
            }
        } else {
            comment.append(Constants.NOTFORSAMEFIELDTEXT);
        }
    }

    private static boolean checkExpYear(User reviewee, User reviewer) {
        return reviewee.getExperienceYears() < reviewer.getExperienceYears();
    }

    private static boolean areCoworkers(User reviewee, User reviewer) {
        return reviewee.getCompanyName().equals(reviewer.getCompanyName());
    }

    private static boolean areSameField(User reviewee, User reviewer) {
        return reviewee.getWorkingArea().equals(reviewer.getWorkingArea());
    }
}
