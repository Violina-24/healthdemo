package com.health.healthdemo.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


import java.security.Principal;

@Controller
@RequestMapping("/application")
public class ApplicationController {

 @Autowired
 private ApplicationService applicationService;

 @Autowired
 private ApplicationService tApplicationService;
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
// @PostMapping("/submit-studentform")
// @ResponseBody
// public ResponseEntity<String> submitStudentForm(@RequestBody TApplication formInput, Principal principal) {
//  if (principal == null) {
//   // Log the message for debugging
//   System.out.println("User is not logged in!");
//   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
//  }
//  String email = principal.getName();
//  MUsers user = mUsersRepository.findByEmail(email).orElseThrow();
//
//  TApplication existingApp = tApplicationRepository.findByUser(user).orElse(new TApplication());
//
//  // Set fields
//  existingApp.setUser(user); // don't forget this
////  existingApp.setName(formInput.getName());
//  existingApp.setDOB(formInput.getDOB());
//  existingApp.setNationality(formInput.getNationality());
//  existingApp.setReligion(formInput.getReligion());
//  existingApp.setGender(formInput.getGender());
//
////  // Set permanent address
////  MPostalAddress permAddr = formInput.getPermanent_Address();
////  if (permAddr != null) {
////   existingApp.setPermanent_Address(permAddr);
////  }
////
////  // Set correspondence address
////  MPostalAddress corrAddr = formInput.getCorrespondence_Address();
////  if (corrAddr != null) {
////   existingApp.setCorrespondence_Address(corrAddr);
////  }
//
//  tApplicationRepository.save(existingApp);
//  return ResponseEntity.ok("Application saved successfully");
// }





 // ✅ 4. Quota page
 @GetMapping("/quota")
 public String showQuotaPage(Model model, Principal principal) {
  MUsers user = usersService.findByEmail(principal.getName());
  TApplication app = tApplicationRepository.findByUser(user).orElse(new TApplication());
  model.addAttribute("tapplication", app);
  return "quota";
 }

// @PostMapping("/save-quota")
// public String saveQuota(@ModelAttribute TApplication tapp, Principal principal) {
//  MUsers user = usersService.findByEmail(principal.getName());
//  TApplication existing = tApplicationRepository.findByUser(user).orElse(new TApplication());
//
//  existing.setCid(tapp.getCid());
//  existing.setInstitute(tapp.getInstitute());
//
//  existing.setUser(user);
//  tApplicationRepository.save(existing);
//
//  return "redirect:/application/qualification";
// }

 // ✅ 5. Qualification page
 @GetMapping("/qualification")
 public String showQualificationPage(Model model, Principal principal) {
  MUsers user = usersService.findByEmail(principal.getName());
  TApplication app = tApplicationRepository.findByUser(user).orElse(new TApplication());
  model.addAttribute("tapplication", app);
  return "qualification";
 }

// @PostMapping("/submit-qualification")
// public String submitQualification(@ModelAttribute TApplication tapplication, Principal principal) {
//  if (principal == null) {
//   return "redirect:/login";
//  }
//  String email = principal.getName();
//  Optional<MUsers> userOptional = mUsersRepository.findByEmail(email);
//
//  if (userOptional.isPresent()) {
//   MUsers user = userOptional.get();
//   TApplication existingApplication = tApplicationRepository.findByUser(user).orElse(new TApplication());
//
//   existingApplication.setPhysics_Score(tapplication.getPhysics_Score());
//   existingApplication.setChemistry_Score(tapplication.getChemistry_Score());
//   existingApplication.setBiology_Biotech_Score(tapplication.getBiology_Biotech_Score());
//   existingApplication.setSubjectChoice(tapplication.getSubjectChoice());
//   existingApplication.setNEET_SCORE(tapplication.getNEET_SCORE());
//
//   existingApplication.setUser(user);
//   tApplicationRepository.save(existingApplication);
//
//   return "redirect:/application/parentsinfo";
//  }
//  return "redirect:/login";
// }


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
//  response.put("name", application.getName());
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
                               @RequestParam("class10and12certificate") MultipartFile certificate,
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
   application.setClass10and12Marksheet(marksheet.getBytes());
   application.setClass10and12certificate(certificate.getBytes());
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
//   if (employerCertificate != null && !employerCertificate.isEmpty()) {
//    application.setEmployer_Certificate(employerCertificate.getBytes());
//   }

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

 @PostMapping("/save")
 public ResponseEntity<TApplication> saveApplication(@RequestBody TApplication tApplication) {
  try {
   System.out.println("controller dob: " + tApplication.getDOB());
   System.out.println("PassportPhoto: " + (tApplication.getPassportPhoto() != null ? "present" : "null"));
   System.out.println("AgeProof: " + (tApplication.getAgeProof() != null ? "present" : "null"));


   tApplication.setStatus("pending");
   TApplication savedApplication = tApplicationService.saveApplication(tApplication);
   return ResponseEntity.ok(savedApplication);
  } catch (RuntimeException ex) {
   ex.printStackTrace();
   return ResponseEntity.badRequest().body(null);
  }
 }





// @PostMapping("/submitAll")
// public ResponseEntity<?> submitApplication(
//         @RequestBody Map<String, Object> allFormData,
//         HttpSession session) {
//
//  String userEmail = (String) session.getAttribute("userEmail");
//
//  if (userEmail == null) {
//   System.out.println("Unauthorized access attempt or session expired.");
//   return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//           .body(Map.of("error", "User not logged in or session expired"));
//  }
//
//  Optional<MUsers> userOpt = mUsersRepository.findByEmail(userEmail);
//
//  if (userOpt.isEmpty()) {
//   return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//           .body(Map.of("error", "User not found"));
//  }
//
//  MUsers user = userOpt.get();
//
//
//  Map<String, Object> studentformForm = (Map<String, Object>) allFormData.get("studentformForm");
//  Map<String, Object> quotaForm = (Map<String, Object>) allFormData.get("quotaForm");
//  Map<String, Object> qualificationForm = (Map<String, Object>) allFormData.get("qualificationForm");
//  Map<String, Object> fileUploadForm = (Map<String, Object>) allFormData.get("fileUploadForm");
//
//  Long applicationId = applicationService.saveApplicationFromForms(
//          studentformForm, quotaForm, qualificationForm, fileUploadForm, userEmail);
//
//  return ResponseEntity.ok(Map.of(
//          "status", "success",
//          "message", "Application submitted successfully",
//          "applicationId", applicationId
//  ));
// }
//

// @PostMapping("/submitAll")
// @ResponseBody
// public ResponseEntity<?> submitApplication(@RequestBody Map<String, Object> fullApplicationData) {
//  try {
//   System.out.println("===== Stage 1: Received Application Submission Request =====");
//   System.out.println("Full Application Data: " + fullApplicationData);
//
//   // Extract sub-forms
//   Map<String, Object> studentForm = (Map<String, Object>) fullApplicationData.get("studentformForm");
//   Map<String, Object> quotaForm = (Map<String, Object>) fullApplicationData.get("quotaForm");
//   Map<String, Object> qualificationForm = (Map<String, Object>) fullApplicationData.get("qualificationForm");
//   Map<String, Object> fileUploadForm = (Map<String, Object>) fullApplicationData.get("fileUploadForm");
//
//   System.out.println("===== Stage 2: Saving Permanent Address =====");
//   Map<String, Object> permAddr = (Map<String, Object>) studentForm.get("permanent_Address");
//   MPostalAddress permanentAddress = new MPostalAddress();
//   permanentAddress.setAddressLine1((String) permAddr.get("addressLine1"));
//   permanentAddress.setAddressLine2((String) permAddr.get("addressLine2"));
//   permanentAddress.setDistrict((String) permAddr.get("district"));
//   permanentAddress.setState((String) permAddr.get("state"));
//   permanentAddress.setPincode(Integer.parseInt(permAddr.get("pincode").toString()));
//   mPostalAddressRepository.save(permanentAddress);
//   System.out.println("Permanent Address Saved: " + permanentAddress);
//
//   System.out.println("===== Stage 3: Saving Correspondence Address =====");
//   Map<String, Object> corrAddr = (Map<String, Object>) studentForm.get("correspondence_Address");
//   MPostalAddress correspondenceAddress = new MPostalAddress();
//   correspondenceAddress.setAddressLine1((String) corrAddr.get("addressLine1"));
//   correspondenceAddress.setAddressLine2((String) corrAddr.get("addressLine2"));
//   correspondenceAddress.setDistrict((String) corrAddr.get("district"));
//   correspondenceAddress.setState((String) corrAddr.get("state"));
//   correspondenceAddress.setPincode(Integer.parseInt(corrAddr.get("pincode").toString()));
//   mPostalAddressRepository.save(correspondenceAddress);
//   System.out.println("Correspondence Address Saved: " + correspondenceAddress);
//
//   System.out.println("===== Stage 4: Creating Application Object =====");
//   TApplication application = new TApplication();
//   application.setName((String) studentForm.get("name"));
//   application.setGender((String) studentForm.get("gender"));
//   application.setNationality((String) studentForm.get("nationality"));
//   application.setReligion((String) studentForm.get("religion"));
//   application.setPermanent_Address(permanentAddress);
//   application.setCorrespondence_Address(correspondenceAddress);
//   System.out.println("Basic Student Info Set");
//
//   System.out.println("===== Stage 5: Setting Quota Info =====");
////   application.setDomicile((String) quotaForm.get("domicile"));
////   application.setState((String) quotaForm.get("state"));
////   application.setCategoryName((String) quotaForm.get("categoryName"));
////   application.setPhysicallyChallenged((String) quotaForm.get("physicallyChallenged"));
////   application.setEws((String) quotaForm.get("ews"));
////   application.setManagementQuota((String) quotaForm.get("managementQuota"));
////   System.out.println("Quota Info Set");
//
//   System.out.println("===== Stage 6: Setting Qualification Info =====");
////   application.setPhysicsMarks(Double.parseDouble((String) qualificationForm.get("physics")));
////   application.setChemistryMarks(Double.parseDouble((String) qualificationForm.get("chemistry")));
////   application.setBiologyMarks(Double.parseDouble((String) qualificationForm.get("biology")));
////   application.setNeetScore((String) qualificationForm.get("neet"));
////   application.setSubjectChoice((String) qualificationForm.get("subjectChoice"));
//   System.out.println("Qualification Info Set");
//
////   System.out.println("===== Stage 7: Setting File Upload (Aadhaar Phone) =====");
////   application.setAadhaarPhone((String) fileUploadForm.get("aadhaarPhone"));
////   System.out.println("File Upload Info Set");
//
//   System.out.println("===== Stage 8: Saving Final Application =====");
//   tApplicationRepository.save(application);
//   System.out.println("Application Saved Successfully: ID = " + application.getA_id());
//
//   Map<String, Object> result = new HashMap<>();
//   result.put("applicationId", application.getA_id());
//   return ResponseEntity.ok(result);
//
//  } catch (Exception e) {
//   e.printStackTrace();
//   System.err.println("===== Error Stage: " + e.getMessage() + " =====");
//   return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//           .body(Collections.singletonMap("error", "Error submitting application: " + e.getMessage()));
//  }
// }


 @GetMapping("/applicationsuccess")
 public String applicationSuccessPage(@RequestParam("applicationId") Long applicationId, Model model) {
  model.addAttribute("applicationId", applicationId);
  return "application_success"; // This should match your HTML file name (application_success.html)
 }

 @GetMapping("/applications")
 public ResponseEntity<List<TApplication>> getAllApplications() {
  List<TApplication> applications = tApplicationRepository.findAll();
  return ResponseEntity.ok(applications);
 }

 @PatchMapping("/applications/{id}/status")
 public ResponseEntity<TApplication> updateApplicationStatus(
         @PathVariable Long id,
         @RequestBody Map<String, String> statusUpdate) {

  Optional<TApplication> optionalApp = tApplicationRepository.findById(id);
  if (optionalApp.isEmpty()) {
   return ResponseEntity.notFound().build();
  }

  TApplication application = optionalApp.get();
  application.setStatus(statusUpdate.get("status"));
  tApplicationRepository.save(application);

  return ResponseEntity.ok(application);
 }

}


