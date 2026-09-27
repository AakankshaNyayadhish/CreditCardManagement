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
import com.ofss.entity.User;

@RestController
public class AuthController {
	
	@Autowired
	private AuthService userService;
	
	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody User user){
		User registeredUser=userService.registerUser(user);
		if(registeredUser==null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
	}
	
	@PostMapping("/login")
	public ResponseEntity<User> loginUser(@RequestParam String email,@RequestParam String password){
		User user=userService.loginUser(email, password);
		if(user==null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
	
	@GetMapping("/users/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable("userId") Long userId){
		User user=userService.getUserById(userId);
		if(user==null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
}
