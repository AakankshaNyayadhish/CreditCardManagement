package com.ofss.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ofss.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
	Optional<User>findByEmailId(String emailId);
	boolean existsByEmailId(String emailId);

}
