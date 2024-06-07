package com.fabhotel.Eras.applicationlayer;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class EndorsementRequest {

	@NotBlank
	private String revieweeUserId;

	@NotBlank
	private String reviewerUserId;

	@NotBlank
	private String skill;

	@NotBlank
	private double score;

}
