package com.health.healthdemo.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for simplicity (ensure you handle CSRF in production, especially with form-based login)
                .csrf(AbstractHttpConfigurer::disable)

                // Enable CORS with proper configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests(auth -> auth
                        // Allow static files without authentication
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**", "/favicon.ico","/api/districts","/application/save")
                        .permitAll()

                        // Allow public API endpoints without authentication
                        .requestMatchers("/", "/api/login", "/register", "/login", "/home",
                                "/favicon.ico", "/studentform", "/application/studentform", "/application/submit-studentform",
                                "/qualification", "/application/qualification", "/application/api/user", "/api/user",
                                "/get-user-details", "/save-student-form", "/api/categories", "/application/parentsinfo",
                                "/parentsinfo", "/index", "/api/documents/**", "/userdetails", "/api/home",
                                "/api/getUserLoginDetails", "/api/get-loginuser-details", "/api/logout",
                                "api/documents/with-course/{dtype}", "api/categories/fetched_categories", "/fileupload",
                                "/quota", "/preview", "/previewApplication", "/forgotpassword", "/application/submitAll","/application/applicationsuccess","/application_success", "/admin-dashboard","/application/applications"
                        , "/application/applications/{id}/status","/courses","/application/applications/course-stats","/application/preview/{id}","/application-details","/application//applications/{id}/documents/{documentType}",
                                "/application/application-details","/myapplications","/application/applications/{id}/download","/application/applications/{id}/full","/application/application-stats")
                        .permitAll() // Allow these endpoints for everyone

                        // Authenticate all other requests (requires login)
                        .anyRequest()
                        .authenticated()
                )

                // Session management (Create session if required, for traditional login handling)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // Create session when needed
                )

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))


                // Form login configuration
                .formLogin(login -> login
                        .loginPage("/login")  // Custom login page URL
                        .defaultSuccessUrl("/home", true)
                        .defaultSuccessUrl("/admin")// Redirect to home page after successful login
                        .permitAll()  // Allow access to login page without authentication
                )

                // Logout configuration
                .logout(logout -> logout
                        .logoutUrl("/logout") // Logout URL
                        .logoutSuccessUrl("/login?logout") // Redirect to login page after logout
                        .permitAll()  // Allow logout without authentication
                )

                // Custom AuthenticationEntryPoint for API responses
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            // Handle unauthenticated requests
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"error\": \"User not logged in\"}");
                        })
                );

        return http.build();
    }

    // CORS configuration to handle cross-origin requests
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8082")); // Or your frontend origin
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); // <== VERY IMPORTANT

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
