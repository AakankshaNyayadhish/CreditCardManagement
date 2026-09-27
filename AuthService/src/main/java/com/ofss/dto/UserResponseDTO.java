package com.ofss.dto;

import com.ofss.enums.UserRole;
import com.ofss.enums.UserStatus;

public class UserResponseDTO {
	private Long userId;
	private String emailId;
	private UserRole role;
	private UserStatus status;
	public UserResponseDTO(Long userId, String emailId, UserRole role, UserStatus status) {
		this.userId = userId;
		this.emailId = emailId;
		this.role = role;
		this.status = status;
	}
	public Long getUserId() {
		return userId;
	}
	public String getEmailId() {
		return emailId;
	}
	public UserRole getRole() {
		return role;
	}
	public UserStatus getStatus() {
		return status;
	}
}