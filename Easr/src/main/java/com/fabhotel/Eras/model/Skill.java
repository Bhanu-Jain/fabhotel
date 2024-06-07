package com.fabhotel.Eras.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Skill {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	@NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Skill parent;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Skill> subSkills = new ArrayList<>();    
}
