package com.fabhotel.Eras.repository;

import com.fabhotel.Eras.model.Skill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
	Skill findByName(String name);
}
