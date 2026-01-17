package com.java.ipm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.java.ipm.dto.AuthRequest;
import com.java.ipm.dto.AuthResponse;
import com.java.ipm.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000") // Allow frontend React app
public class AuthController {

	@Autowired
	private UserService userService;

	/**
	 * Register a new user
	 */
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody AuthRequest request) {
		try {
			String message = userService.register(request);
			return ResponseEntity.ok(message);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * Login user and return JWT token
	 */
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
		try {
			AuthResponse response = userService.login(request);
			return ResponseEntity.ok(response);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(AuthResponse.withMessage("Login failed: " + e.getMessage()));
		}
	}
}
