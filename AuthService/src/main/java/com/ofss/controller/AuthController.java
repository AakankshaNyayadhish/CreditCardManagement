package com.ofss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ofss.Service.AuthService;
import com.ofss.dto.LoginRequest;
import com.ofss.dto.UserResponseDTO;
import com.ofss.entity.User;

@RestController
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/register")
	public ResponseEntity<UserResponseDTO> registerUser(@RequestBody User user){
		UserResponseDTO registeredUser=authService.registerUser(user);
		if(registeredUser==null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
	}
	
	@PostMapping("/login")
	public ResponseEntity<UserResponseDTO> loginUser(@RequestBody LoginRequest loginRequest){
		UserResponseDTO user=authService.loginUser(loginRequest.getEmailId(),loginRequest.getPassword());
		if(user==null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
	
	@GetMapping("/users/{userId}")
	public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("userId") Long userId){
		UserResponseDTO user=authService.getUserById(userId);
		if(user==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
}
