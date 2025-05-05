package com.health.healthdemo.controller;

import com.health.healthdemo.entity.MCategory;
import com.health.healthdemo.entity.MPostalAddress;
import com.health.healthdemo.entity.MUsers;
import com.health.healthdemo.entity.TApplication;
import com.health.healthdemo.repository.MCategoryRepository;
import com.health.healthdemo.repository.MPostalAddressRepository;
import com.health.healthdemo.repository.MUsersRepository;
import com.health.healthdemo.repository.TApplicationRepository;
import com.health.healthdemo.services.ApplicationService;
import com.health.healthdemo.services.UsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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
 @Autowired
 private MCategoryRepository  mCategoryRepository;
 @Autowired
 MPostalAddressRepository mPostalAddressRepository;

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
 @GetMapping("/studentform")
 public String loadStudentForm(Model model, Principal principal) {
  MUsers user = mUsersRepository.findByEmail(principal.getName()).orElseThrow();
  TApplication application = tApplicationRepository.findByUser(user)
          .orElse(new TApplication());
  model.addAttribute("tapplication", application);
  return "studentform";
 }


 // ✅ 3. Save student form (API called by JavaScript)
 @PostMapping("/submit-studentform")
 @ResponseBody
 public ResponseEntity<String> submitStudentForm(@RequestBody TApplication formInput, Principal principal) {
  if (principal == null) {
   // Log the message for debugging
   System.out.println("User is not logged in!");
   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
  }
  String email = principal.getName();
  MUsers user = mUsersRepository.findByEmail(email).orElseThrow();

  TApplication existingApp = tApplicationRepository.findByUser(user).orElse(new TApplication());

  // Set fields
  existingApp.setUser(user); // don't forget this
  existingApp.setName(formInput.getName());
  existingApp.setDOB(formInput.getDOB());
  existingApp.setNationality(formInput.getNationality());
  existingApp.setReligion(formInput.getReligion());
  existingApp.setGender(formInput.getGender());

  // Set permanent address
  MPostalAddress permAddr = formInput.getPermanent_Address();
  if (permAddr != null) {
   existingApp.setPermanent_Address(permAddr);
  }

  // Set correspondence address
  MPostalAddress corrAddr = formInput.getCorrespondence_Address();
  if (corrAddr != null) {
   existingApp.setCorrespondence_Address(corrAddr);
  }

  tApplicationRepository.save(existingApp);
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
 @PostMapping("/api/preview")
 @ResponseBody
 public ResponseEntity<?> previewApplicationApi(@ModelAttribute TApplication application,
                                                Principal principal) {
  if (principal == null) {
   return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
           .body(Map.of("error", "User not logged in"));
  }

  String email = principal.getName();
  Optional<MUsers> userOpt = mUsersRepository.findByEmail(email);
  if (userOpt.isEmpty()) {
   return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
           .body(Map.of("error", "User not found"));
  }

  MUsers user = userOpt.get();
  application.setUser(user);

  // Return preview-relevant data
  Map<String, Object> response = new HashMap<>();
  response.put("name", application.getName());
  response.put("dob", application.getDOB());
  response.put("email", user.getEmail());
  response.put("phone", user.getPhone());
  // add more fields as needed

  return ResponseEntity.ok(response);
 }



 @PostMapping("/upload-documents")
 public String uploadDocuments(@RequestParam("passportPhoto") MultipartFile passportPhoto,
                               @RequestParam("ageProof") MultipartFile ageProof,
                               @RequestParam("class10and12Marksheet") MultipartFile marksheet,
                               @RequestParam(value = "casteCertificate", required = false) MultipartFile casteCertificate,
                               @RequestParam(value = "pwdCertificate", required = false) MultipartFile pwdCertificate,
                               @RequestParam("neetResult") MultipartFile neetResult,
                               @RequestParam("characterCertificate") MultipartFile characterCertificate,
                               @RequestParam(value = "prc", required = false) MultipartFile prc,
                               @RequestParam(value = "employerCertificate", required = false) MultipartFile employerCertificate,
                               Principal principal) {
  if (principal == null) return "redirect:/login";

  MUsers user = usersService.findByEmail(principal.getName());
  if (user == null) return "redirect:/login";

  TApplication application = tApplicationRepository.findByUser(user).orElse(new TApplication());
  application.setUser(user);

  try {
   application.setPassportPhoto(passportPhoto.getBytes());
   application.setAgeProof(ageProof.getBytes());
   application.setClassXIIMarksheet(marksheet.getBytes());
   if (casteCertificate != null && !casteCertificate.isEmpty()) {
    application.setCaste_Certificate(casteCertificate.getBytes());
   }
   if (pwdCertificate != null && !pwdCertificate.isEmpty()) {
    application.setPWD_Certificate(pwdCertificate.getBytes());
   }
   application.setNeet_Results(neetResult.getBytes());
   application.setCharacter_Certificate(characterCertificate.getBytes());
   if (prc != null && !prc.isEmpty()) {
    application.setPrc(prc.getBytes());
   }
   if (employerCertificate != null && !employerCertificate.isEmpty()) {
    application.setEmployer_Certificate(employerCertificate.getBytes());
   }

   tApplicationRepository.save(application);
  } catch (Exception e) {
   e.printStackTrace();
   return "redirect:/application/document-upload?error=true";
  }

  return "redirect:/application/preview";
 }

 @GetMapping("/application/view-file/{type}")
 public ResponseEntity<byte[]> viewFile(@PathVariable String type, Principal principal) {
  if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

  MUsers user = usersService.findByEmail(principal.getName());
  if (user == null) return ResponseEntity.notFound().build();

  TApplication application = tApplicationRepository.findByUser(user)
          .orElseThrow(() -> new RuntimeException("Application not found"));

  byte[] fileData;
  String contentType;

  switch (type) {
   case "passportPhoto":
    fileData = application.getPassportPhoto();
    contentType = "image/jpeg"; // or "image/png" as needed
    break;
   case "ageProof":
    fileData = application.getAgeProof();
    contentType = "application/pdf";
    break;
   // add more cases for other files...
   default:
    return ResponseEntity.badRequest().build();
  }

  return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(contentType))
          .body(fileData);
 }
// @PostMapping("/submit")
// public ResponseEntity<String> saveApplication(@RequestBody Map<String, Object> formData, HttpSession session) {
//  String email = (String) session.getAttribute("userEmail");
//
//  if (email == null || !email.equals(formData.get("email"))) {
//   return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email mismatch or session expired.");
//  }
//
//  TApplication application = new TApplication();
//
//  // Student Form
//  application.setName((String) formData.get("name"));
//  application.setemail((String) formData.get("email"));
//  application.setphone((String) formData.get("phone"));
//  application.setDOB(LocalDate.parse((String) formData.get("dob")));
//  application.setNationality((int) formData.get("nationality"));
//  application.setReligion((String) formData.get("religion"));
//  application.setGender((String) formData.get("gender"));
//
//  // Quota Page
////  application.setDomicile((String) formData.get("domicile"));
////  application.setCategory((String) formData.get("category"));
////  application.setInstitute((String) formData.get("institute"));
////  application.setCourse((String) formData.get("course"));
//
//  // Qualification Page
//  application.setNeetMarks((String) formData.get("neetMarks"));
//  application.setBoard10((String) formData.get("board10"));
//  application.setBoard12((String) formData.get("board12"));
//  application.setPassYear10((String) formData.get("passYear10"));
//  application.setPassYear12((String) formData.get("passYear12"));
//
//  // Document Upload
//  application.setPhotoPath((String) formData.get("photoPath"));
//  application.setMarksheetPath((String) formData.get("marksheetPath"));
//  application.setCasteCertPath((String) formData.get("casteCertPath"));
//  application.setNeetResultPath((String) formData.get("neetResultPath"));
//
//  // Permanent Address
//  Map<String, Object> perm = (Map<String, Object>) formData.get("permanent_Address");
//  application.setPermAddress1((String) perm.get("addressLine1"));
//  application.setPermAddress2((String) perm.get("addressLine2"));
//  application.setPermDistrict((String) perm.get("district"));
//  application.setPermState((String) perm.get("state"));
//  application.setPermPincode((String) perm.get("pincode"));
//
//  // Correspondence Address
//  Map<String, Object> corr = (Map<String, Object>) formData.get("correspondence_Address");
//  application.setCorrAddress1((String) corr.get("addressLine1"));
//  application.setCorrAddress2((String) corr.get("addressLine2"));
//  application.setCorrDistrict((String) corr.get("district"));
//  application.setCorrState((String) corr.get("state"));
//  application.setCorrPincode((String) corr.get("pincode"));
//
//  tApplicationRepository.save(application);
//  return ResponseEntity.ok("All form data submitted successfully.");
// }

}


