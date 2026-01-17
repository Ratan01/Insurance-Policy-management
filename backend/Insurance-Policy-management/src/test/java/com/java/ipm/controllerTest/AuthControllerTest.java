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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.ipm.controller.AuthController;
import com.java.ipm.dto.AuthRequest;
import com.java.ipm.dto.AuthResponse;
import com.java.ipm.service.UserService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;
    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();

        authRequest = new AuthRequest();
        authRequest.setUsername("john_doe");
        authRequest.setPassword("password123");
        authRequest.setEmail("john@example.com");
    }

    // ✅ Test successful registration
    @Test
    void register_Success() throws Exception {
        when(userService.register(any(AuthRequest.class))).thenReturn("User registered successfully!");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully!"));
    }

    // ❌ Test registration failure when username already exists
    @Test
    void register_Failure_UsernameExists() throws Exception {
        when(userService.register(any(AuthRequest.class))).thenThrow(new RuntimeException("Username already exists"));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())  // ✅ Expect 400 Bad Request
                .andExpect(content().string("Username already exists"));
    }


    // ✅ Test successful login
    @Test
    void login_Success() throws Exception {
        AuthResponse authResponse = new AuthResponse("mocked-jwt-token");
        when(userService.login(any(AuthRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }

    // ❌ Test login failure when credentials are invalid
    @Test
    void login_Failure_InvalidCredentials() throws Exception {
        when(userService.login(any(AuthRequest.class))).thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized())  // ✅ Expect 401 Unauthorized
                .andExpect(jsonPath("$.token").value("Invalid credentials"));
    }
}
