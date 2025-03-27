package com.health.healthdemo.controller;


import com.health.healthdemo.entity.MUsers;
import com.health.healthdemo.repository.MUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@Controller
public class UserController {
    @Autowired
    private MUsersRepository usersRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Matches login.html inside templates/
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> loginUser(@RequestBody MUsers user) {
        Optional<MUsers> foundUser = usersRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());

        if (foundUser.isPresent()) {
            return ResponseEntity.ok().body("{\"message\": \"Login Successful\"}");
        } else {
            return ResponseEntity.status(401).body("{\"message\": \"Invalid credentials\"}");
        }
    }


    @GetMapping("/register")
    public String registerPage() {
        return "register"; // Matches register.html inside templates/
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> registerUser(@RequestBody MUsers user) {
        usersRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

//@PostMapping("/register")
//public ResponseEntity<String> registerUser(@RequestBody MUsers newUser) {
//    if (newUser.getEmail() == null || newUser.getPassword() == null) {
//        return ResponseEntity.badRequest().body("Invalid Data");
//    }
//    usersRepository.save(newUser);
//    return ResponseEntity.ok("Account created successfully!");
//}

    @GetMapping("/home")
    public String homePage() {
        return "home"; // This will load home.html
    }

    @GetMapping("/studentform")
    public String studentForm() {
        return "studentform"; // This will load application-form.html
    }


    @GetMapping("/get-user-details")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
        }

        Optional<MUsers> userOpt = usersRepository.findByEmail(authentication.getName());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }

        MUsers user = userOpt.get();
        Map<String, String> userData = Map.of(
                "name", user.getName(),
                "email", user.getEmail(),
                "phone", user.getPhone()
        );

        return ResponseEntity.ok(userData); // Ensures JSON response
    }
}


