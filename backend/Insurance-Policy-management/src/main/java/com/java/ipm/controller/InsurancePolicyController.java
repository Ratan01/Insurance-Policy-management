package com.java.ipm.controller;

import com.java.ipm.entity.InsurancePolicy;
import com.java.ipm.service.InsurancePolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/policies")
public class InsurancePolicyController {

    @Autowired
    private InsurancePolicyService policyService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<InsurancePolicy> getAllPolicy() {
        return policyService.getAllPolicies();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<InsurancePolicy> getPolicyById(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(policyService.getPolicyById(id, authentication));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<InsurancePolicy> createPolicy(@RequestBody InsurancePolicy policy, Authentication authentication) {
        return ResponseEntity.ok(policyService.createPolicy(policy, authentication));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<InsurancePolicy>> getMyPolicies(Authentication authentication) {
        return ResponseEntity.ok(policyService.getPoliciesByUser(authentication));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<InsurancePolicy> updatePolicy(@PathVariable Long id, @RequestBody InsurancePolicy policy,
                                                        Authentication authentication) {
        return ResponseEntity.ok(policyService.updatePolicy(id, policy, authentication));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> deletePolicy(@PathVariable Long id, Authentication authentication) {
        policyService.deletePolicy(id, authentication);
        return ResponseEntity.ok("Policy deleted successfully");
    }
}
