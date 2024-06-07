package com.fabhotel.Eras.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.fabhotel.Eras.ExceptionHandler.ValidationException;
import com.fabhotel.Eras.applicationlayer.EndorsementRequest;
import com.fabhotel.Eras.applicationlayer.EndorsementResponse;
import com.fabhotel.Eras.model.Endorsement;
import com.fabhotel.Eras.service.EndorsementService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/endorsements")
@Validated
public class EndorsementController {
    private static final Logger logger = LoggerFactory.getLogger(EndorsementController.class);

    private final EndorsementService endorsementService;
    private final EndorsementResponse endorsementResponse;

    @Autowired
    public EndorsementController(EndorsementService endorsementService, EndorsementResponse endorsementResponse) {
        this.endorsementService = endorsementService;
        this.endorsementResponse = endorsementResponse;
    }
    private void validateEndorsementRequest(EndorsementRequest request) {
        if (request.getScore() == 0 || request.getScore()<0) {
            throw new ValidationException("Score must not be zero or less than zero");
        }
        if (request.getSkill() == null || request.getSkill().isEmpty()) {
            throw new ValidationException("Skill must not be blank");
        }
    }

    @PostMapping
    @ApiOperation(value = "Post a new endorsement")
    public EndorsementResponse postEndorsement(@Valid @RequestBody  EndorsementRequest request) {
    	// Call the validation method
        validateEndorsementRequest(request);
    	Endorsement endorsement = endorsementService.postEndorsement(request.getRevieweeUserId(),
                request.getReviewerUserId(),
                request.getSkill(),
                request.getScore());
        endorsementResponse.setAdjustedScore(endorsement.getAdjustedScore());
        endorsementResponse.setAdjustmentReason(endorsement.getComment());
        return endorsementResponse;
    }

	/*
	 * @GetMapping("/{userId}")
	 * 
	 * @ApiOperation(value = "Get endorsements for a user", response =
	 * Endorsement.class, responseContainer = "List") public List<Endorsement>
	 * getEndorsements(@PathVariable @NotBlank String userId) {
	 * logger.info("Received request to get endorsements for user {}", userId);
	 * return endorsementService.getEndorsements(userId); }
	 */
    @GetMapping("/{userId}")
    @ApiOperation(value = "Get endorsements for a user", response = Endorsement.class, responseContainer = "List")
    public Map<String, List<String>> getEndorsements(@PathVariable @NotBlank String userId) {
       
        return endorsementService.getEndorsements(userId);
    }
}