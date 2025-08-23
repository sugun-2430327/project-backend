package com.autoinsurance.insurance.controller;

import com.autoinsurance.insurance.dto.UserResponse;
import com.autoinsurance.insurance.model.User;
import com.autoinsurance.insurance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:4200"})
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Get current user profile
     */
    @GetMapping("/profile")
    public ResponseEntity<User> getCurrentUserProfile() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Get all users with detailed information including income and ID proof
     * Admin access only
     */
    @GetMapping("/detailed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsersDetailed(Principal principal) {
        try {
            List<UserResponse> users = userService.getAllUsersDetailed(principal);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    /**
     * Get specific user with detailed information including income and ID proof
     * Admin access only
     */
    @GetMapping("/detailed/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserDetailed(@PathVariable Long userId, Principal principal) {
        try {
            UserResponse user = userService.getUserDetailedById(userId, principal);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    /**
     * Get all customers with detailed information including income and ID proof
     * Admin access only
     */
    @GetMapping("/customers/detailed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllCustomersDetailed(Principal principal) {
        try {
            List<UserResponse> customers = userService.getAllCustomersDetailed(principal);
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    

}
