package com.autoinsurance.insurance.service;

import com.autoinsurance.insurance.dto.UserResponse;
import com.autoinsurance.insurance.model.Role;
import com.autoinsurance.insurance.model.User;
import com.autoinsurance.insurance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Long findUserIdByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(User::getUserId).orElse(null);
    }

    public List<User> getAllAgents() {
        return userRepository.findByRole(Role.AGENT);
    }

    public List<User> getAllCustomers() {
        return userRepository.findByRole(Role.CUSTOMER);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        return (User) authentication.getPrincipal();
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    /**
     * Get all users with detailed information - Admin and Agent access only
     */
    public List<UserResponse> getAllUsersDetailed(Principal principal) {
        User currentUser = getCurrentUserFromPrincipal(principal);
        
        // Check if user has permission (ADMIN or AGENT)
        if (currentUser.getRole() != Role.ADMIN && currentUser.getRole() != Role.AGENT) {
            throw new RuntimeException("Access denied. Only admins and agents can view user details.");
        }
        
        return userRepository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Get user by ID with detailed information - Admin and Agent access only
     */
    public UserResponse getUserDetailedById(Long userId, Principal principal) {
        User currentUser = getCurrentUserFromPrincipal(principal);
        
        // Check if user has permission (ADMIN or AGENT)
        if (currentUser.getRole() != Role.ADMIN && currentUser.getRole() != Role.AGENT) {
            throw new RuntimeException("Access denied. Only admins and agents can view user details.");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        return new UserResponse(user);
    }
    
    /**
     * Get all customers with detailed information - Admin and Agent access only
     */
    public List<UserResponse> getAllCustomersDetailed(Principal principal) {
        User currentUser = getCurrentUserFromPrincipal(principal);
        
        // Check if user has permission (ADMIN or AGENT)
        if (currentUser.getRole() != Role.ADMIN && currentUser.getRole() != Role.AGENT) {
            throw new RuntimeException("Access denied. Only admins and agents can view customer details.");
        }
        
        return userRepository.findByRole(Role.CUSTOMER).stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Helper method to get current user from Principal
     */
    private User getCurrentUserFromPrincipal(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Current user not found"));
    }
}
