package com.java.ipm.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.java.ipm.dto.AuthRequest;
import com.java.ipm.dto.AuthResponse;
import com.java.ipm.entity.User;
import com.java.ipm.repository.UserRepository;
import com.java.ipm.security.JwtUtil;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	@Autowired private UserRepository userRepository;
	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private AuthenticationManager authenticationManager;
	@Autowired private JwtUtil jwtUtil;

	public String register(AuthRequest request) {
		Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
		if (existingUser.isPresent()) throw new RuntimeException("Username already exists");

		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setEmail(request.getEmail());
		user.setRole(request.getRole() != null ? request.getRole() : "USER");

		userRepository.save(user);
		return "User registered successfully!";
	}

	public AuthResponse login(AuthRequest request) {
		// Hard-coded admin
		if ("admin".equals(request.getUsername())) {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken("admin", request.getPassword())
			);
			User admin = new User();
			admin.setUsername("admin");
			admin.setRole("ADMIN");
			String token = jwtUtil.generateToken(admin);
			return new AuthResponse(token, admin.getUsername(), admin.getRole());
		}

		// Regular user
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
		);

		User user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));

		String token = jwtUtil.generateToken(user);
		return new AuthResponse(token, user.getUsername(), user.getRole());
	}

	public List<User> getAllUsers() { return userRepository.findAll(); }

	// Get a user by ID
	public User getUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found"));
	}

	// Delete a user
	public void deleteUser(Long id) {
		User user = getUserById(id);
		userRepository.delete(user);
	}

}
