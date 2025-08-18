package com.autoinsurance.insurance.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:4200"})
@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }
    
    @GetMapping("/customer")
    @PreAuthorize("hasRole('CUSTOMER') or hasAuthority('CUSTOMER')")
    public String customerAccess() {
        return "Customer Content.";
    }

    @GetMapping("/agent")
    @PreAuthorize("hasRole('AGENT') or hasAuthority('AGENT')")
    public String agentAccess() {
        return "Agent Content.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ADMIN')")
    public String adminAccess() {
        return "Admin Content.";
    }
}
