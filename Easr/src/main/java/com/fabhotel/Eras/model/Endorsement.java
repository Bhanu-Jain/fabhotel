package com.fabhotel.Eras.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Endorsement {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private double score;
	private double adjustedScore; 
	private String comment;
	private String skillName;
	@ManyToOne
	@JoinColumn(name="reviewer_id")
	private User reviewer;
	@ManyToOne
	@JoinColumn(name = "reviewee_id")
	private User reviewee;
	
	@ManyToOne
	@JoinColumn(name="skill_id")
	private Skill skill;

	

}
