package com.ofss.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ofss.entity.User;
import com.ofss.enums.UserRole;
import com.ofss.enums.UserStatus;
import com.ofss.repository.UserRepository;

@Service
public class AuthService {
	@Autowired
	private UserRepository userRepository;
	
	public User registerUser(User user) {
		if(userRepository.existsByEmailId(user.getEmailId())) {
			return null;
		}
		user.setRole(UserRole.USER);
		user.setStatus(UserStatus.ACTIVE);
		return userRepository.save(user);
	}
	
	public User loginUser(String email,String password) {
		User user=userRepository.findByEmailId(email).orElse(null);
		if(user==null) {
			return null;
		}
		if(!user.getPassword().equals(password)) {
			return null;
		}
		if(user.getStatus()==UserStatus.BLOCKED) {
			return null;
		}
		return user;
	}
	
	public User getUserById(Long userId) {
		return userRepository.findById(userId).orElse(null);
	}
}