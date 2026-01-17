package com.java.ipm.controllerTest;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.ipm.controller.InsurancePolicyController;
import com.java.ipm.entity.InsurancePolicy;
import com.java.ipm.entity.User;
import com.java.ipm.service.InsurancePolicyService;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class InsurancePolicyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InsurancePolicyService policyService;

    @InjectMocks
    private InsurancePolicyController policyController;

    private ObjectMapper objectMapper;
    private InsurancePolicy testPolicy;
    private User testUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(policyController).build();
        objectMapper = new ObjectMapper();

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("john_doe");

        testPolicy = new InsurancePolicy();
        testPolicy.setId(100L);
        testPolicy.setPolicyName("Health Insurance");
        testPolicy.setPolicyType("Health");
        testPolicy.setPremiumAmount(5000.0f);
        testPolicy.setUser(testUser);
    }

    // ✅ Test get all policies (Admin Access)
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // ✅ Simulate an admin user
    void getAllPolicies_Success_Admin() throws Exception {
        when(policyService.getAllPolicies()).thenReturn(List.of(testPolicy));

        mockMvc.perform(get("/api/policies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    // ✅ Test get policy by ID (Owner or Admin)
    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"}) // ✅ Simulate a logged-in user
    void getPolicyById_Success_OwnerOrAdmin() throws Exception {
        when(policyService.getPolicyById(eq(100L), any())).thenReturn(testPolicy);

        mockMvc.perform(get("/api/policies/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.policyName").value("Health Insurance"));
    }

    // ❌ Test get policy by ID (Unauthorized)
//    @Test
//    @WithMockUser(username = "unauthorized_user", roles = {"USER"}) // ❌ Unauthorized user
//    void getPolicyById_Failure_Unauthorized() throws Exception {
//        when(policyService.getPolicyById(eq(100L), any())).thenThrow(new RuntimeException("Access denied"));
//
//        mockMvc.perform(get("/api/policies/100"))
//                .andExpect(status().isForbidden());
//    }

    // ✅ Test create policy (User Access)
    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"}) // ✅ Simulate a logged-in user
    void createPolicy_Success_User() throws Exception {
        when(policyService.getAuthenticatedUser(any())).thenReturn(testUser);
        when(policyService.createPolicy(any(), any())).thenReturn(testPolicy);

        mockMvc.perform(post("/api/policies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPolicy)))
                .andExpect(status().isOk());
    }

//    // ❌ Test create policy (Unauthorized)
//    @Test
//    @WithMockUser(username = "unauthorized_user", roles = {"GUEST"}) // ❌ Unauthorized role
//    void createPolicy_Failure_Unauthorized() throws Exception {
//        when(policyService.createPolicy(any(), any())).thenThrow(new RuntimeException("Access denied"));
//
//        mockMvc.perform(post("/api/policies")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(testPolicy)))
//                .andExpect(status().isForbidden());
//    }

    // ✅ Test update policy (Owner Access)
    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"}) // ✅ Simulate a logged-in user
    void updatePolicy_Success_Owner() throws Exception {
        when(policyService.updatePolicy(eq(100L), any(), any())).thenReturn(testPolicy);

        mockMvc.perform(put("/api/policies/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPolicy)))
                .andExpect(status().isOk());
    }

    // ❌ Test update policy (Unauthorized)
//    @Test
//    @WithMockUser(username = "unauthorized_user", roles = {"USER"}) // ❌ Unauthorized user
//    void updatePolicy_Failure_Unauthorized() throws Exception {
//        when(policyService.updatePolicy(eq(100L), any(), any())).thenThrow(new RuntimeException("Unauthorized access"));
//
//        mockMvc.perform(put("/api/policies/100")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(testPolicy)))
//                .andExpect(status().isForbidden());
//    }

    // ✅ Test delete policy (Owner Access)
    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"}) // ✅ Simulate a logged-in user
    void deletePolicy_Success_Owner() throws Exception {
        doNothing().when(policyService).deletePolicy(eq(100L), any());

        mockMvc.perform(delete("/api/policies/100"))
                .andExpect(status().isOk())
                .andExpect(content().string("Policy deleted successfully"));
    }

    // ❌ Test delete policy (Unauthorized)
//    @Test
//    @WithMockUser(username = "unauthorized_user", roles = {"USER"}) // ❌ Unauthorized user
//    void deletePolicy_Failure_Unauthorized() throws Exception {
//        doThrow(new RuntimeException("Unauthorized access")).when(policyService).deletePolicy(eq(100L), any());
//
//        mockMvc.perform(delete("/api/policies/100"))
//                .andExpect(status().isForbidden());
//    }
}
