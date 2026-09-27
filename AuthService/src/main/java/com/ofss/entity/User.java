package com.ofss.entity;

import com.ofss.enums.UserRole;
import com.ofss.enums.UserStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	private String emailId;
	private String password;
	private UserRole role;
	private UserStatus status;
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(Long userId, String emailId, String password, UserRole role, UserStatus status) {
		super();
		this.userId = userId;
		this.emailId = emailId;
		this.password = password;
		this.role = role;
		this.status = status;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public UserRole getRole() {
		return role;
	}
	public void setRole(UserRole role) {
		this.role = role;
	}
	public UserStatus getStatus() {
		return status;
	}
	public void setStatus(UserStatus status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", emailId=" + emailId + ", password=" + password + ", role=" + role
				+ ", status=" + status + "]";
	}
	
}
