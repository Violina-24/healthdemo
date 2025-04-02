package com.health.healthdemo.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF for simplicity (not recommended for production)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**").permitAll() // Allow static files
                        .requestMatchers("/", "/api/login", "/register", "/login", "/home",
                                "/favicon.ico", "/studentform", "/qualification", "/application/qualification",
                                "/application/api/user", "/api/user", "/get-user-details", "/save-student-form",
                                "/api/categories", "/application/parentsinfo", "/parentsinfo", "/index",
                                "/api/documents/**", "/api/documents*", "/api/documents", "/username") // Allow API endpoints
                        .permitAll()
                        .anyRequest().authenticated() // Authenticate all other requests
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // Ensure session is created
                )
                .formLogin(login -> login
                        .loginPage("/login") // Correct login page URL
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // Custom AuthenticationEntryPoint for API responses
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"error\": \"User not logged in\"}");
                        })
                )
                .cors(cors -> cors.disable()); // Disable CORS to allow cross-origin requests

        return http.build();
    }
}
