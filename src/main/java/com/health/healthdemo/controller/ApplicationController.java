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

 // ✅ 1. API endpoint for frontend (fetch user details)
 @GetMapping("/api/user")
 @ResponseBody
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

 // ✅ 2. Show student form page
 @GetMapping("/student-form")
 public String showStudentForm(Model model, Principal principal) {
  if (principal == null) {
   return "redirect:/login";
  }
  String email = principal.getName();
  Optional<MUsers> user = mUsersRepository.findByEmail(email);

  if (user.isPresent()) {
   model.addAttribute("user", user.get());
   TApplication existingApp = tApplicationRepository.findByUser(user.get()).orElse(new TApplication());
   model.addAttribute("tapplication", existingApp);
   return "studentform";
  } else {
   return "redirect:/login";
  }
 }

 // ✅ 3. Save student form (API called by JavaScript)
 @PostMapping("/save-student-form")
 @ResponseBody
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

 // ✅ 4. Quota page
 @GetMapping("/quota")
 public String showQuotaPage(Model model, Principal principal) {
  MUsers user = usersService.findByEmail(principal.getName());
  TApplication app = tApplicationRepository.findByUser(user).orElse(new TApplication());
  model.addAttribute("tapplication", app);
  return "quota";
 }

 @PostMapping("/save-quota")
 public String saveQuota(@ModelAttribute TApplication tapp, Principal principal) {
  MUsers user = usersService.findByEmail(principal.getName());
  TApplication existing = tApplicationRepository.findByUser(user).orElse(new TApplication());

  existing.setCategoryname(tapp.getCategoryname());
  existing.setInstitute(tapp.getInstitute());

  existing.setUser(user);
  tApplicationRepository.save(existing);

  return "redirect:/application/qualification";
 }

 // ✅ 5. Qualification page
 @GetMapping("/qualification")
 public String showQualificationPage(Model model, Principal principal) {
  MUsers user = usersService.findByEmail(principal.getName());
  TApplication app = tApplicationRepository.findByUser(user).orElse(new TApplication());
  model.addAttribute("tapplication", app);
  return "qualification";
 }

 @PostMapping("/submit-qualification")
 public String submitQualification(@ModelAttribute TApplication tapplication, Principal principal) {
  if (principal == null) {
   return "redirect:/login";
  }
  String email = principal.getName();
  Optional<MUsers> userOptional = mUsersRepository.findByEmail(email);

  if (userOptional.isPresent()) {
   MUsers user = userOptional.get();
   TApplication existingApplication = tApplicationRepository.findByUser(user).orElse(new TApplication());

   existingApplication.setPhysics_Score(tapplication.getPhysics_Score());
   existingApplication.setChemistry_Score(tapplication.getChemistry_Score());
   existingApplication.setBiology_Biotech_Score(tapplication.getBiology_Biotech_Score());
   existingApplication.setSubjectChoice(tapplication.getSubjectChoice());
   existingApplication.setNEET_SCORE(tapplication.getNEET_SCORE());

   existingApplication.setUser(user);
   tApplicationRepository.save(existingApplication);

   return "redirect:/application/parentsinfo";
  }
  return "redirect:/login";
 }
}


