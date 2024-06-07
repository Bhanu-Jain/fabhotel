package com.fabhotel.Eras.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Data
public class User {
	
	@Id	
	@NotBlank
	private String userId;
	@Min(0)
	private int experienceYears;
	
	private String skill;
	private String workingArea;
	private String companyName;

}
