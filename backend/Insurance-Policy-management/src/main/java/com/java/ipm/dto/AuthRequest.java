package com.java.ipm.dto;

public class AuthRequest {
	private String username;
	private String password;
	private String email; // optional for registration
	private String role;  // optional for registration

	// getters & setters
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getRole() { return role; }
	public void setRole(String role) { this.role = role; }
}
