package com.java.ipm.controller;

import com.java.ipm.entity.User;
import com.java.ipm.entity.InsurancePolicy;
import com.java.ipm.service.UserService;
import com.java.ipm.service.InsurancePolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InsurancePolicyService policyService;

    // Get all users (ADMIN only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get a specific user by ID (ADMIN only)
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // Delete a user (ADMIN only)
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    // Get all policies of a user (ADMIN only)
    @GetMapping("/{userId}/policies")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InsurancePolicy>> getUserPolicies(@PathVariable Long userId) {
        List<InsurancePolicy> policies = policyService.getPoliciesByUserId(userId);
        return ResponseEntity.ok(policies);
    }
}
