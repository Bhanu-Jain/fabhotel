package com.fabhotel.Eras.repository;

import java.util.List;

import com.fabhotel.Eras.model.Endorsement;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EndorsementRepository extends JpaRepository<Endorsement, Long> {
	//List<Endorsement> findByRevieweeId(String revieweeUserId);
	List<Endorsement> findByRevieweeUserId(String revieweeUserId);

}
