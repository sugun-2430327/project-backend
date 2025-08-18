package com.autoinsurance.insurance.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileServingConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Static file serving for uploads directory
        // This allows direct access to files via /api/uploads/{filename}
        // Note: This should be used with caution in production for security
        registry.addResourceHandler("/api/uploads/**")
                .addResourceLocations("file:uploads/");
                
        // Alternative for id-proofs specifically
        registry.addResourceHandler("/api/uploads/id-proofs/**")
                .addResourceLocations("file:uploads/id-proofs/");
    }
}
