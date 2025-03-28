package com.health.healthdemo.config;

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
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**","/uploads/**").permitAll() // ✅ Allow static files
                        .requestMatchers("/", "/api/login", "/register", "/login", "/home",
                                "/favicon.ico", "/studentform", "/qualification", "/application/qualification",
                                "/application/api/user", "api/user", "/api/get-user-details", "/save-student-form",
                                "/api/categories", "application/parentsinfo", "/parentsinfo", "/index",
                                "/api/documents/**", "/api/documents*", "/api/documents")  // ✅ Allow access to API endpoints for documents
                        .permitAll()
                        .requestMatchers("/api/get-user-details").authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

}
