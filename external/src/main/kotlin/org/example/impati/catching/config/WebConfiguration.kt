package org.example.impati.catching.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/v1/**")
            .allowedOrigins(
                "http://localhost:5173",
                "http://127.0.0.1:5173",
            )
            .allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
    }
}
