package com.ofss.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ofss.dto.UserResponseDTO;
import com.ofss.entity.User;
import com.ofss.enums.UserRole;
import com.ofss.enums.UserStatus;
import com.ofss.repository.UserRepository;

@Service
public class AuthService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public UserResponseDTO registerUser(User user) {
		if(userRepository.existsByEmailId(user.getEmailId())) {
			return null;
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(UserRole.USER);
		user.setStatus(UserStatus.ACTIVE);
		User savedUser=userRepository.save(user);
		return mapToDTO(savedUser);
	}
	
	public UserResponseDTO loginUser(String email,String rawPassword) {
		User user=userRepository.findByEmailId(email).orElse(null);
		if(user==null) {
			return null;
		}
		if(!passwordEncoder.matches(rawPassword,user.getPassword())) {
			return null;
		}
		if(user.getStatus()==UserStatus.BLOCKED) {
			return null;
		}
		return mapToDTO(user);
	}
	
	public UserResponseDTO getUserById(Long userId) {
		 User user=userRepository.findById(userId).orElse(null);
		 if(user!=null) {
			 return mapToDTO(user);
		 }
		 return null;
	}
	
	private UserResponseDTO mapToDTO(User user) {
		return new UserResponseDTO(user.getUserId(),user.getEmailId(),user.getRole(),user.getStatus());
	}
}