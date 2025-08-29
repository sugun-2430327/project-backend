package com.autoinsurance.insurance.service;

import com.autoinsurance.insurance.dto.JwtResponse;
import com.autoinsurance.insurance.dto.LoginRequest;
import com.autoinsurance.insurance.dto.RegisterRequest;
import com.autoinsurance.insurance.model.Role;
import com.autoinsurance.insurance.model.User;
import com.autoinsurance.insurance.repository.UserRepository;
import com.autoinsurance.insurance.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        try {
            String usernameOrEmail = loginRequest.getUsernameOrEmail();
            
            // Find user by username or email
            User userDetails = findUserByUsernameOrEmail(usernameOrEmail)
                    .orElseThrow(() -> new RuntimeException("User not found: " + usernameOrEmail));
            
            // Use the actual username for authentication (Spring Security expects username)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDetails.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            
            return new JwtResponse(
                    jwt, 
                    userDetails.getUserId(), 
                    userDetails.getUsername(), 
                    userDetails.getEmail(), 
                    userDetails.getRole());
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }
    }

    public User registerUser(RegisterRequest registerRequest) {
        // Check if username exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        // Check if email exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        // Create new user with all fields
        User user = new User(
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getUsername(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getEmail(),
                registerRequest.getRole() != null ? registerRequest.getRole() : Role.CUSTOMER,
                registerRequest.getAge(),
                registerRequest.getIdProofFilePath());

        return userRepository.save(user);
    }

    /**
     * Find user by username or email
     */
    private Optional<User> findUserByUsernameOrEmail(String usernameOrEmail) {
        // Check if input looks like an email (contains @)
        if (usernameOrEmail.contains("@")) {
            return userRepository.findByEmail(usernameOrEmail);
        } else {
            return userRepository.findByUsername(usernameOrEmail);
        }
    }
}
