package com.java.ipm.service;

import com.java.ipm.entity.User;
import com.java.ipm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Do NOT inject PasswordEncoder here — it causes circular dependency
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final String ADMIN_USERNAME = "admin";
    private final String ADMIN_PASSWORD = "admin123";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // HARD CODED ADMIN
        if (ADMIN_USERNAME.equals(username)) {
            return org.springframework.security.core.userdetails.User
                    .withUsername(ADMIN_USERNAME)
                    .password(encoder.encode(ADMIN_PASSWORD))
                    .authorities("ROLE_ADMIN")
                    .build();
        }

        // LOAD USER FROM DB
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole())
                .build();
    }
}
