package com.fabhotel.Eras.service;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fabhotel.Eras.model.Endorsement;
import com.fabhotel.Eras.model.Skill;
import com.fabhotel.Eras.model.User;
import com.fabhotel.Eras.repository.EndorsementRepository;
import com.fabhotel.Eras.repository.SkillRepository;
import com.fabhotel.Eras.repository.UserRepository;
import com.fabhotel.Eras.util.Constants;
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
	private static SkillRepository skillRepository;
	@Autowired
	private EndorsementRepository endorsementRepository;

	@Autowired
	public EndorsementService(UserRepository userRepository, SkillRepository skillRepository,
			EndorsementRepository endorsementRepository) {
		this.userRepository = userRepository;
		this.skillRepository = skillRepository;
		this.endorsementRepository = endorsementRepository;
	}

	@Transactional
	public Endorsement postEndorsement(@NotBlank String revieweeUserId, @NotBlank String reviewerUserId,
			@NotBlank String skillName, @NotBlank double score) {
		logger.debug("Fetching reviewee with ID: {}", revieweeUserId);
		User reviewee = getUserById(revieweeUserId, Constants.ERROR_REVIEWEE_NOT_FOUND);

		logger.debug("Fetching reviewer with ID: {}", reviewerUserId);
		User reviewer = getUserById(reviewerUserId, Constants.ERROR_REVIEWER_NOT_FOUND);

		logger.debug("Fetching skill with name: {}", skillName);
		Skill skill = getSkillByName(skillName, Constants.ERROR_SKILL_NOT_FOUND);
		double skillScore = getSkillScore(TechSkill.valueOf(reviewer.getSkill()),
				TechSkill.valueOf(reviewee.getSkill()));
		TadjustedScore adjustedScore = calculateAdjustedScore(reviewer, reviewee, skillName, skillScore);

		Endorsement endorsement = new Endorsement();
		endorsement.setAdjustedScore(adjustedScore.getAdjustedScore());
		endorsement.setComment(adjustedScore.getComment());
		endorsement.setScore(score);
		endorsement.setReviewee(reviewee);
		endorsement.setReviewer(reviewer);
		endorsement.setSkill(skill);

		logger.debug("Saving endorsement");
		return endorsementRepository.save(endorsement);
	}

	public static int getSkillScore(TechSkill endorserSkill, TechSkill endorsedSkill) {
		if (endorserSkill == endorsedSkill) {
			return Constants.EXACT_SKILL_MATCH; // Exact skill match
		} else if (SkillHierarchyUtil.isBroaderSkill(endorserSkill, endorsedSkill)) {
			return Constants.BRODER_SKILL_MATCH; // Broader skill match
		} else if (SkillHierarchyUtil.isBroaderSkill(endorsedSkill, endorserSkill)) {
			return Constants.SUB_SKILL_SAME_BRODER_MATCH; // Different sub-skill under the same broader skill
		} else {
			return Constants.SKILL_MISMATCH; // Completely different skill
		}
	}

	private User getUserById(String userId, String errorMessage) {
		return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(errorMessage));
	}

	private Skill getSkillByName(String skillName, String errorMessage) {
		return skillRepository.findByName(skillName);

	}

	public List<Endorsement> getEndorsements(@NotBlank String userId) {
		logger.debug("Fetching endorsements for user with ID: {}", userId);
		return endorsementRepository.findByRevieweeUserId(userId);
	}

	public static TadjustedScore calculateAdjustedScore(User reviewer, User reviewee, String skill, double skillScore) {
		double baseScore = Constants.BASE_SCORE; // Base score for the reviewer
		double adjustedScore = baseScore;
		StringBuilder comment = new StringBuilder("Base score: " + baseScore);
		boolean isCoworker = areCoworkers(reviewee, reviewer);
		boolean isSameField = areSameField(reviewee, reviewer);
		boolean checkExpYear = checkExpYear(reviewee, reviewer);

		adjustedScore += skillScore;
		comment.append(", +").append("skillScore").append(" An additional score is added for their shared skill proficiency.\n");

		if (isCoworker) {
			adjustedScore += Constants.COWORKER_BONUS;
			comment.append(", +").append(Constants.COWORKER_BONUS).append(" users are coworkers from the same company, an additional bonus is applied\n");
		} else {
			comment.append(", Not a coworker, no bonus applied\n");
		}

		if (isSameField) {
			if (checkExpYear) {
				adjustedScore += Constants.EXPERIENCE_BONUS;
				adjustedScore += Constants.SAME_FIELD_BONUS;
				comment.append(", +").append(Constants.SAME_FIELD_BONUS).append(" additional score is awarded due to their shared professional field\n");
				comment.append(", +").append(Constants.EXPERIENCE_BONUS).append(" An extra score is granted due to one Reviewer greater experience in the shared professional field.\n");
			} else {
				adjustedScore += Constants.SAME_FIELD_BONUS;
				comment.append(", +").append(Constants.SAME_FIELD_BONUS).append("An additional score is allocated because both users share the same professional field.\n");
			}
		} else {
			comment.append(", Not from the same field, no bonus applied\n");
		}

		return new TadjustedScore(adjustedScore, comment.toString());
	}

	private static boolean checkExpYear(User reviewee, User reviewer) {
		// TODO: Implement method to check if reviewer has more experience in the same
		// field as reviewee
		int revieweeExperience = reviewee.getExperienceYears(); // Assuming getExperience() returns a map of field names
																// to experience years
		int reviewerExperience = reviewer.getExperienceYears();

		if (revieweeExperience < reviewerExperience) {
			return true;
		}

		return false;
	}

	private static boolean areCoworkers(User reviewee, User reviewer) {
		// TODO: Implement method to check if reviewee and reviewer are coworkers
		if (reviewee.getCompanyName().equals(reviewer.getCompanyName())) {
			return true;
		}
		return false;
	}

	private static boolean areSameField(User reviewee, User reviewer) {
		// TODO: Implement method to check if reviewee and reviewer are in the same
		// field

		if (reviewee.getWorkingArea().equals(reviewer.getWorkingArea())) {
			return true;
		}
		return false;
	}
}
