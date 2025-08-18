package com.autoinsurance.insurance.controller;

import com.autoinsurance.insurance.dto.JwtResponse;
import com.autoinsurance.insurance.dto.LoginRequest;
import com.autoinsurance.insurance.dto.RegisterRequest;
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
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam(value = "incomePerAnnum", required = false) Double incomePerAnnum,
            @RequestParam(value = "idProof", required = false) MultipartFile idProofFile) {
        
        try {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUsername(username);
            registerRequest.setPassword(password);
            registerRequest.setEmail(email);
            registerRequest.setIncomePerAnnum(incomePerAnnum);
            registerRequest.setRole(null); // Will default to CUSTOMER in service
            
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
            // Set default role if not provided
            if (registerRequest.getRole() == null) {
                registerRequest.setRole(null); // Will default to CUSTOMER in service
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
