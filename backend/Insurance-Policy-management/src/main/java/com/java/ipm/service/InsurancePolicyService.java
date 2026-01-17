package com.java.ipm.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.java.ipm.entity.InsurancePolicy;
import com.java.ipm.entity.User;
import com.java.ipm.repository.InsurancePolicyRepository;
import com.java.ipm.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class InsurancePolicyService {

    @Autowired
    private InsurancePolicyRepository insurancePolicyRepository;

    @Autowired
    private UserRepository userRepository;

    public List<InsurancePolicy> getAllPolicies() {
        return insurancePolicyRepository.findAll();
    }

    public InsurancePolicy getPolicyById(Long id, Authentication authentication) {
        InsurancePolicy policy = insurancePolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        if (authentication == null) return policy; // admin path may pass null if used that way

        User currentUser = getAuthenticatedUser(authentication);

        if (!policy.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("Access denied");
        }

        return policy;
    }

    @Transactional
    public InsurancePolicy createPolicy(InsurancePolicy policy, Authentication authentication) {
        User currentUser = getAuthenticatedUser(authentication);
        policy.setUser(currentUser);
        return insurancePolicyRepository.save(policy);
    }

    @Transactional
    public InsurancePolicy updatePolicy(Long id, InsurancePolicy updatedPolicy, Authentication authentication) {
        InsurancePolicy existingPolicy = insurancePolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        User currentUser = getAuthenticatedUser(authentication);

        if (!existingPolicy.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("Unauthorized access");
        }

        existingPolicy.setPolicyName(updatedPolicy.getPolicyName());
        existingPolicy.setPolicyType(updatedPolicy.getPolicyType());
        existingPolicy.setPremiumAmount(updatedPolicy.getPremiumAmount());

        return insurancePolicyRepository.save(existingPolicy);
    }

    @Transactional
    public void deletePolicy(Long id, Authentication authentication) {
        InsurancePolicy policy = insurancePolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        User currentUser = getAuthenticatedUser(authentication);

        if (!policy.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("Unauthorized access");
        }
        insurancePolicyRepository.delete(policy);
    }

    public List<InsurancePolicy> getPoliciesByUser(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return insurancePolicyRepository.findByUserId(user.getId());
    }

    public List<InsurancePolicy> getPoliciesByUserId(Long userId) {
        return insurancePolicyRepository.findByUserId(userId);
    }

    public User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Authentication object is null or username is missing!");
        }

        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found in DB for username: " + username));
    }
}
