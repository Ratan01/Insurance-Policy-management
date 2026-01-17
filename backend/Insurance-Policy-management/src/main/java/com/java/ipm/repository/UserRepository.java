package com.java.ipm.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.java.ipm.entity.User;


public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}