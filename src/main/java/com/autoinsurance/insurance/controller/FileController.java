package com.autoinsurance.insurance.controller;

import com.autoinsurance.insurance.model.Role;
import com.autoinsurance.insurance.model.User;
import com.autoinsurance.insurance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:4200"})
@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private UserService userService;

    private static final String UPLOAD_DIR = "uploads/id-proofs/";

    /**
     * View file in browser (for images/PDFs)
     * Only accessible by ADMIN and AGENT roles
     */
    @GetMapping("/view/{fileName:.+}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<Resource> viewFile(@PathVariable String fileName, Principal principal) {
        try {
            // Validate user permissions
            validateUserAccess(principal);
            
            // Load file as Resource
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("File not found: " + fileName);
            }

            // Determine content type
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid file path: " + fileName, e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + fileName, e);
        }
    }

    /**
     * Download file
     * Only accessible by ADMIN and AGENT roles
     */
    @GetMapping("/download/{fileName:.+}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, Principal principal) {
        try {
            // Validate user permissions
            validateUserAccess(principal);
            
            // Load file as Resource
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("File not found: " + fileName);
            }

            // Force download with proper headers
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid file path: " + fileName, e);
        }
    }

    /**
     * Get file by full path (extracts filename from path)
     * Only accessible by ADMIN and AGENT roles
     */
    @GetMapping("/view-by-path")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<Resource> viewFileByPath(@RequestParam String filePath, Principal principal) {
        try {
            // Extract filename from path
            String fileName = Paths.get(filePath).getFileName().toString();
            return viewFile(fileName, principal);
        } catch (Exception e) {
            throw new RuntimeException("Error accessing file: " + filePath, e);
        }
    }

    /**
     * Download file by full path (extracts filename from path)
     * Only accessible by ADMIN and AGENT roles
     */
    @GetMapping("/download-by-path")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<Resource> downloadFileByPath(@RequestParam String filePath, Principal principal) {
        try {
            // Extract filename from path
            String fileName = Paths.get(filePath).getFileName().toString();
            return downloadFile(fileName, principal);
        } catch (Exception e) {
            throw new RuntimeException("Error downloading file: " + filePath, e);
        }
    }

    /**
     * Validate that the current user has permission to access files
     */
    private void validateUserAccess(Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (currentUser.getRole() != Role.ADMIN && currentUser.getRole() != Role.AGENT) {
            throw new RuntimeException("Access denied. Only admins and agents can access uploaded files.");
        }
    }

    /**
     * Check if file exists
     * Only accessible by ADMIN and AGENT roles
     */
    @GetMapping("/exists/{fileName:.+}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public ResponseEntity<Boolean> fileExists(@PathVariable String fileName, Principal principal) {
        try {
            validateUserAccess(principal);
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
            boolean exists = Files.exists(filePath);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
}
