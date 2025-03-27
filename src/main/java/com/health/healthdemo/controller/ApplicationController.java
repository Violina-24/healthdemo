package com.health.healthdemo.controller;

import com.health.healthdemo.entity.MUsers;
import com.health.healthdemo.entity.TApplication;
import com.health.healthdemo.repository.MUsersRepository;
import com.health.healthdemo.repository.TApplicationRepository;
import com.health.healthdemo.services.ApplicationService;
import com.health.healthdemo.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;


import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;

@Controller
@RequestMapping("/application")
public class ApplicationController {

 @Autowired
 private ApplicationService applicationService;

 @Autowired
 private MUsersRepository mUsersRepository;

 @Autowired
 private TApplicationRepository tApplicationRepository;

 @Autowired
 private UsersService usersService;

 @GetMapping("/api/user")
 public ResponseEntity<Map<String, String>> getUserDetails(Authentication authentication) {
  if (authentication == null) {
   return ResponseEntity.status(401).build();
  }

  String email = authentication.getName();
  Optional<MUsers> user = mUsersRepository.findByEmail(email);

  if (user.isPresent()) {
   Map<String, String> userData = new HashMap<>();
   userData.put("name", user.get().getName());
   userData.put("email", user.get().getEmail());
   userData.put("phone", user.get().getPhone());
   return ResponseEntity.ok(userData);
  } else {
   return ResponseEntity.notFound().build();
  }
 }



 @GetMapping("/student-form")
 public String showStudentForm(Model model, Principal principal) {
  if (principal == null) {
   return "redirect:/login";
  }

  String email = principal.getName();
  Optional<MUsers> user = mUsersRepository.findByEmail(email);

  if (user.isPresent()) {
   model.addAttribute("user", user.get());
  } else {
   return "redirect:/login";
  }

  model.addAttribute("tapplication", new TApplication());
  return "studentform";
 }

 @RestController
 @RequestMapping("/api")
 public class UserController {

  @GetMapping("/get-user-details")
  public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
   if (userDetails == null) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not logged in"));
   }

   MUsers user = usersService.findByEmail(userDetails.getUsername()); // Assuming email is username
   if (user == null) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
   }

   return ResponseEntity.ok(Map.of(
           "name", user.getName(),
           "email", user.getEmail(),
           "phone", user.getPhone()
   ));
  }
 }

 @PostMapping("/submit-application")
 public String submitApplication(@ModelAttribute TApplication tapplication, Principal principal) {
//  if (principal == null) {
//   return "redirect:/login";
//  }

  String email = principal.getName();
  Optional<MUsers> user = mUsersRepository.findByEmail(email);

  if (user.isEmpty()) {
   return "redirect:/login";
  }
  tapplication.setUser(user.get());

  return "redirect:/success";
 }

 @PostMapping("/save-student-form")
 public ResponseEntity<String> saveStudentForm(@ModelAttribute TApplication application, Principal principal) {
  if (principal == null || principal.getName() == null) {
   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
  }

  MUsers user = usersService.findByEmail(principal.getName());

  if (user == null) {
   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
  }

  application.setUser(user);
  tApplicationRepository.save(application);

  return ResponseEntity.ok("Application saved successfully");
 }

 @GetMapping("/qualification")
 public String qualificationPage(){
  return "/qualification"; // Loads the qualification.html page
 }


 @PostMapping("/submit-qualification")
 public String submitQualification(@ModelAttribute TApplication tapplication, Principal principal) {
  if (principal == null) {
   return "redirect:/login";
  }

  String email = principal.getName();
  Optional<MUsers> userOptional = mUsersRepository.findByEmail(email);

  if (userOptional.isEmpty()) {
   return "redirect:/login"; // Prevent saving if user not found
  }

  MUsers user = userOptional.get();
  TApplication existingApplication = tApplicationRepository.findByUser(user).orElse(new TApplication());

  // Update fields
  existingApplication.setPhysics_Score(tapplication.getPhysics_Score());
  existingApplication.setChemistry_Score(tapplication.getChemistry_Score());
  existingApplication.setBiology_Biotech_Score(tapplication.getBiology_Biotech_Score());
  existingApplication.setSubjectChoice(tapplication.getSubjectChoice());
  existingApplication.setNEET_SCORE(tapplication.getNEET_SCORE());

  existingApplication.setUser(user); // Ensure user is set
  tApplicationRepository.save(existingApplication);

  return "redirect:/application/parentsinfo"; // Change to your actual next page
 }

 @GetMapping("/parentsinfo")
 public String parentsinfoPage(){
  return "parentsinfo"; // ✅ Correct
 }

}
