package com.java.ipm.serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import com.java.ipm.entity.InsurancePolicy;
import com.java.ipm.entity.User;
import com.java.ipm.repository.InsurancePolicyRepository;
import com.java.ipm.repository.UserRepository;
import com.java.ipm.service.InsurancePolicyService;

@ExtendWith(MockitoExtension.class)
class InsurancePolicyServiceTest {

    @Mock
    private InsurancePolicyRepository insurancePolicyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private InsurancePolicyService insurancePolicyService;

    private User testUser;
    private User adminUser;
    private InsurancePolicy testPolicy;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("john_doe");
        testUser.setRole(com.java.ipm.domain.Role.USER);

        adminUser = new User();
        adminUser.setId(2L);
        adminUser.setUsername("admin");
        adminUser.setRole(com.java.ipm.domain.Role.ADMIN);

        testPolicy = new InsurancePolicy();
        testPolicy.setId(100L);
        testPolicy.setPolicyName("Health Insurance");
        testPolicy.setPolicyType("Health");
        testPolicy.setPremiumAmount(5000.0f);
        testPolicy.setUser(testUser);
    }

    // ✅ Test get all policies
    @Test
    void getAllPolicies_Success() {
        List<InsurancePolicy> policies = Arrays.asList(testPolicy, new InsurancePolicy());
        when(insurancePolicyRepository.findAll()).thenReturn(policies);

        List<InsurancePolicy> response = insurancePolicyService.getAllPolicies();
        assertEquals(2, response.size());
    }

    // ✅ Test get policy by ID (User is Owner)
    @Test
    void getPolicyById_Success_UserIsOwner() {
        when(authentication.getName()).thenReturn(testUser.getUsername());
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(insurancePolicyRepository.findById(testPolicy.getId())).thenReturn(Optional.of(testPolicy));

        InsurancePolicy response = insurancePolicyService.getPolicyById(testPolicy.getId(), authentication);
        assertNotNull(response);
        assertEquals(testPolicy.getId(), response.getId());
    }

    // ✅ Test get policy by ID (User is Admin)
    @Test
    void getPolicyById_Success_UserIsAdmin() {
        when(authentication.getName()).thenReturn(adminUser.getUsername());
        when(userRepository.findByUsername(adminUser.getUsername())).thenReturn(Optional.of(adminUser));
        when(insurancePolicyRepository.findById(testPolicy.getId())).thenReturn(Optional.of(testPolicy));

        InsurancePolicy response = insurancePolicyService.getPolicyById(testPolicy.getId(), authentication);
        assertNotNull(response);
    }

    // ❌ Test get policy by ID (Unauthorized User)
    @Test
    void getPolicyById_Failure_UnauthorizedUser() {
        User anotherUser = new User();
        anotherUser.setId(3L);
        anotherUser.setUsername("another_user");
        anotherUser.setRole(com.java.ipm.domain.Role.USER);

        when(authentication.getName()).thenReturn(anotherUser.getUsername());
        when(userRepository.findByUsername(anotherUser.getUsername())).thenReturn(Optional.of(anotherUser));
        when(insurancePolicyRepository.findById(testPolicy.getId())).thenReturn(Optional.of(testPolicy));

        Exception exception = assertThrows(RuntimeException.class, () -> 
            insurancePolicyService.getPolicyById(testPolicy.getId(), authentication));

        assertEquals("Access denied", exception.getMessage());
    }

    // ✅ Test create policy
    @Test
    void createPolicy_Success() {
        when(authentication.getName()).thenReturn(testUser.getUsername());
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(insurancePolicyRepository.save(any(InsurancePolicy.class))).thenReturn(testPolicy);

        InsurancePolicy response = insurancePolicyService.createPolicy(testPolicy, authentication);
        assertNotNull(response);
        assertEquals(testUser.getId(), response.getUser().getId());
    }

    // ✅ Test update policy (User is Owner)
    @Test
    void updatePolicy_Success_UserIsOwner() {
        when(authentication.getName()).thenReturn(testUser.getUsername());
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(insurancePolicyRepository.findById(testPolicy.getId())).thenReturn(Optional.of(testPolicy));
        when(insurancePolicyRepository.save(any(InsurancePolicy.class))).thenReturn(testPolicy);

        InsurancePolicy updatedPolicy = new InsurancePolicy();
        updatedPolicy.setPolicyName("Updated Health Insurance");
        updatedPolicy.setPolicyType("Updated Health");
        updatedPolicy.setPremiumAmount(6000.0f);

        InsurancePolicy response = insurancePolicyService.updatePolicy(testPolicy.getId(), updatedPolicy, authentication);
        assertEquals("Updated Health Insurance", response.getPolicyName());
    }

    // ❌ Test update policy (Unauthorized User)
    @Test
    void updatePolicy_Failure_UnauthorizedUser() {
        User anotherUser = new User();
        anotherUser.setId(3L);
        anotherUser.setUsername("another_user");

        when(authentication.getName()).thenReturn(anotherUser.getUsername());
        when(userRepository.findByUsername(anotherUser.getUsername())).thenReturn(Optional.of(anotherUser));
        when(insurancePolicyRepository.findById(testPolicy.getId())).thenReturn(Optional.of(testPolicy));

        Exception exception = assertThrows(RuntimeException.class, () -> 
            insurancePolicyService.updatePolicy(testPolicy.getId(), new InsurancePolicy(), authentication));

        assertEquals("Unauthorized access", exception.getMessage());
    }

    // ✅ Test delete policy (User is Owner)
    @Test
    void deletePolicy_Success_UserIsOwner() {
        when(authentication.getName()).thenReturn(testUser.getUsername());
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(insurancePolicyRepository.findById(testPolicy.getId())).thenReturn(Optional.of(testPolicy));

        assertDoesNotThrow(() -> insurancePolicyService.deletePolicy(testPolicy.getId(), authentication));
        verify(insurancePolicyRepository, times(1)).delete(testPolicy);
    }

    // ❌ Test delete policy (Unauthorized User)
    @Test
    void deletePolicy_Failure_UnauthorizedUser() {
        User anotherUser = new User();
        anotherUser.setId(3L);
        anotherUser.setUsername("another_user");

        when(authentication.getName()).thenReturn(anotherUser.getUsername());
        when(userRepository.findByUsername(anotherUser.getUsername())).thenReturn(Optional.of(anotherUser));
        when(insurancePolicyRepository.findById(testPolicy.getId())).thenReturn(Optional.of(testPolicy));

        Exception exception = assertThrows(RuntimeException.class, () -> 
            insurancePolicyService.deletePolicy(testPolicy.getId(), authentication));

        assertEquals("Unauthorized access", exception.getMessage());
    }
}
