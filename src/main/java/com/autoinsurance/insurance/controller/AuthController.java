package com.autoinsurance.insurance.controller;

import com.autoinsurance.insurance.dto.JwtResponse;
import com.autoinsurance.insurance.dto.LoginRequest;
import com.autoinsurance.insurance.dto.RegisterRequest;
import com.autoinsurance.insurance.model.Role;
import com.autoinsurance.insurance.model.User;
import com.autoinsurance.insurance.service.AuthService;
import com.autoinsurance.insurance.service.FileUploadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:4200"})
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    
    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            // Let the GlobalExceptionHandler handle the exception
            throw e;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam(value = "role", defaultValue = "CUSTOMER") String role,
            @RequestParam(value = "age", required = false) Integer age,
            @RequestParam(value = "idProof", required = false) MultipartFile idProofFile) {
        
        try {
            // Validate role parameter
            if (!"CUSTOMER".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
                return ResponseEntity.badRequest().body("Invalid role. Allowed roles: CUSTOMER, ADMIN");
            }
            
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setFirstName(firstName);
            registerRequest.setLastName(lastName);
            registerRequest.setUsername(username);
            registerRequest.setPassword(password);
            registerRequest.setEmail(email);
            registerRequest.setAge(age);
            registerRequest.setRole(Role.valueOf(role.toUpperCase()));
            
            // Handle file upload if provided
            if (idProofFile != null && !idProofFile.isEmpty()) {
                String filePath = fileUploadService.uploadIdProof(idProofFile);
                registerRequest.setIdProofFilePath(filePath);
            }
            
            User user = authService.registerUser(registerRequest);
            return ResponseEntity.ok("User " + user.getUsername() + " registered successfully as " + user.getRole() + "!");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    // Alternative JSON-only registration endpoint (for testing without file upload)
    @PostMapping("/register-json")
    public ResponseEntity<?> registerJson(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Set default role if not provided or validate if provided
            if (registerRequest.getRole() == null) {
                registerRequest.setRole(Role.CUSTOMER); // Default to CUSTOMER
            } else if (registerRequest.getRole() != Role.CUSTOMER && registerRequest.getRole() != Role.ADMIN) {
                return ResponseEntity.badRequest().body("Invalid role. Allowed roles: CUSTOMER, ADMIN");
            }
            User user = authService.registerUser(registerRequest);
            return ResponseEntity.ok("User " + user.getUsername() + " registered successfully as " + user.getRole() + "!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        // JWT is stateless, so we don't need to invalidate the token on the server
        // The client should remove the token from local storage
        return ResponseEntity.ok("Logged out successfully!");
    }
}
