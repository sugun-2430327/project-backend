package com.autoinsurance.insurance.controller;

import com.autoinsurance.insurance.dto.AgentDashboardResponse;
import com.autoinsurance.insurance.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/agents")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @GetMapping("/{id}/dashboard")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<AgentDashboardResponse> getAgentDashboard(
            @PathVariable String id,
            Principal principal) {
        
        AgentDashboardResponse dashboard = agentService.getAgentDashboard(id, principal);
        return ResponseEntity.ok(dashboard);
    }
}
