package com.fabhotel.Eras.repository;

import com.fabhotel.Eras.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
