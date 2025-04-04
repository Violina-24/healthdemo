package com.health.healthdemo.controller;


import com.health.healthdemo.entity.MUsers;
import com.health.healthdemo.repository.MUsersRepository;
import com.health.healthdemo.services.DocumentsService;
import com.health.healthdemo.services.UsersService;
import jakarta.servlet.http.HttpSession;
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

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Controller
public class UserController {
    @Autowired
    private MUsersRepository usersRepository;
    @Autowired
    private UsersService usersService;

    @Autowired
    private DocumentsService documentService;

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Matches login.html inside templates/
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> loginUser(@RequestBody MUsers user, HttpSession session) {
        Optional<MUsers> foundUser = usersRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());

        if (foundUser.isPresent()) {
            MUsers loggedInUser = foundUser.get();

            // Store the entire user object in the session
            session.setAttribute("loggedInUser", loggedInUser);
            System.out.println("User logged in: " + loggedInUser.getEmail());

            return ResponseEntity.ok().body("{\"message\": \"Login Successful\"}");
        } else {
            return ResponseEntity.status(401).body("{\"message\": \"Invalid credentials\"}");
        }
    }
    @PostMapping("/api/logout")
    public ResponseEntity<?> logoutUser(HttpSession session) {
        // Invalidate the session to log out the user
        session.invalidate();
        return ResponseEntity.ok().body("{\"message\": \"Logout Successful\"}");
    }


//@PostMapping("/api/login")
//public ResponseEntity<?> loginUser(@RequestBody MUsers user, HttpSession session) {
//    Optional<MUsers> foundUser = usersRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
//    if (foundUser.isPresent()) {
//        MUsers loggedInUser = foundUser.get();
//
//        // Store the entire user object in the session
//        session.setAttribute("loggedInUser", loggedInUser);
//        System.out.println("User logged in: " + loggedInUser.getEmail());
//
//        return ResponseEntity.ok().body("{\"message\": \"Login Successful\"}");
//    } else {
//        return ResponseEntity.status(401).body("{\"message\": \"Invalid credentials\"}");
//    }
//}

    @GetMapping("/api/get-loginuser-details")
    public ResponseEntity<?> getUserDetails(HttpSession session) {
        MUsers user = (MUsers) session.getAttribute("loggedInUser");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
        }

        Map<String, String> userData = Map.of(
                "userId", String.valueOf(user.getUid()),   // Assuming getId() returns user ID
                "userName", user.getName(),
                "email", user.getEmail(),
                "phone", user.getPhone(),
                    "dob",user.getDob());


        return ResponseEntity.ok(userData);
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
@GetMapping("/api/home")
@ResponseBody
public Map<String, Object> homepage(Authentication authentication) {
    Map<String, Object> response = new HashMap<>();

    if (authentication != null) {
        String email = authentication.getName();
        System.out.println("Authenticated user: " + email);
        MUsers user = usersService.findByEmail(email);

        if (user != null) {
            response.put("name", user.getName());
            response.put("advertisements", documentService.getDocumentsByType("Advertisement"));
            System.out.println("User found: " + user.getName());
        } else {
            response.put("name", "Guest");
        }
    } else {
        response.put("name", "Guest");
    }

    return response;
}




    @GetMapping("/studentform")
    public String studentForm() {
        return "studentform"; // This will load application-form.html
    }

//    @GetMapping("/get-user-details")
//    public ResponseEntity<?> getUserDetails(Authentication authentication) {
//        if (authentication == null || authentication.getName() == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(Map.of("error", "User not authenticated"));
//        }
//
//        Optional<MUsers> userOpt = usersRepository.findByEmail(authentication.getName());
//        if (userOpt.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(Map.of("error", "User not found"));
//        }
//
//        MUsers user = userOpt.get();
//        Map<String, String> userData = Map.of(
//                "name", user.getName(),
//                "email", user.getEmail(),
//                "phone", user.getPhone()
//        );
//
//        return ResponseEntity.ok(userData); // Ensures JSON response
//    }

//    @GetMapping("/user")
//        public ResponseEntity<Map<String, String>> getUserDetails(Principal principal) {
//            Map<String, String> userDetails = new HashMap<>();
//            userDetails.put("name", principal.getName()); // Fetch user's name
//            return ResponseEntity.ok(userDetails);
//        }


    @GetMapping("/api/userdetails")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getUserDetails(Principal principal) {
        Map<String, String> response = new HashMap<>();

        if (principal != null) {
            String email = principal.getName();
            MUsers user = usersService.findByEmail(email);
            if (user != null) {
                response.put("name", user.getName());
                response.put("email", user.getEmail());
                response.put("phone", user.getPhone());
                response.put("dob", user.getDob());
                return ResponseEntity.ok(response);
            }
        }

        response.put("name", "Guest");
        return ResponseEntity.ok(response);
    }



}




