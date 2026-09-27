package com.ofss;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ofss.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	
	Optional<User> findByEmailId(String emailId);
	boolean existsByEmailId(String emailId);
}