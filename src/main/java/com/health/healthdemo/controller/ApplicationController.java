package com.health.healthdemo.controller;

import com.health.healthdemo.entity.MPostalAddress;
import com.health.healthdemo.entity.MUsers;
import com.health.healthdemo.entity.TApplication;
import com.health.healthdemo.repository.*;
import com.health.healthdemo.services.ApplicationService;
import com.health.healthdemo.services.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
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
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayOutputStream;
import java.util.*;


import java.security.Principal;
import java.util.stream.Collectors;

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
 @Autowired
 private MCourseRepository courseRepository;

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

 // Add this to your ApplicationController
 @GetMapping("/applications/course-stats")
 public ResponseEntity<Map<String, Map<String, Long>>> getApplicationStatsByCourse() {
  Map<String, Map<String, Long>> stats = new HashMap<>();

  // Get stats for all courses
  Map<String, Long> allStats = new HashMap<>();
  allStats.put("total", tApplicationRepository.count());
  allStats.put("accepted", tApplicationRepository.countByStatus("accepted"));
  allStats.put("rejected", tApplicationRepository.countByStatus("rejected"));
  stats.put("all", allStats);

  // Get stats for each course
  courseRepository.findAll().forEach(course -> {
   Map<String, Long> courseStats = new HashMap<>();
   courseStats.put("total", tApplicationRepository.countApplicationsByCourseId(course.getCourseid()));
   courseStats.put("accepted", tApplicationRepository.countApplicationsByCourseIdAndStatus(course.getCourseid(), "accepted"));
   courseStats.put("rejected", tApplicationRepository.countApplicationsByCourseIdAndStatus(course.getCourseid(), "rejected"));
   stats.put(course.getCourseid().toString(), courseStats);
  });

  return ResponseEntity.ok(stats);
 }

 @GetMapping("/applications")
 public ResponseEntity<List<Map<String, Object>>> getApplications(
         @RequestParam(required = false) Long courseId) {

  List<TApplication> applications;
  if (courseId != null) {
   applications = tApplicationRepository.findApplicationsByCourseId(courseId);
  } else {
   applications = tApplicationRepository.findAll();
  }

  // Transform the data to match frontend expectations
  List<Map<String, Object>> response = applications.stream().map(app -> {
   Map<String, Object> appData = new HashMap<>();
   appData.put("id", app.getA_id());

   // Get user details
   if (app.getUser() != null) {
    appData.put("applicantName", app.getUser().getName());
    appData.put("email", app.getUser().getEmail());
   } else {
    appData.put("applicantName", "N/A");
    appData.put("email", "N/A");
   }

   // Get course details
   if (app.getmCourse() != null) {
    appData.put("courseId", app.getmCourse().getCourseid());
    appData.put("courseName", app.getmCourse().getCoursename());
   } else {
    appData.put("courseId", null);
    appData.put("courseName", "N/A");
   }

   appData.put("status", app.getStatus() != null ? app.getStatus() : "pending");
   appData.put("appliedDate", app.getCreatedDate()); // Make sure you have this field in TApplication

   return appData;
  }).collect(Collectors.toList());

  return ResponseEntity.ok(response);
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


 @GetMapping("/applications/{id}/documents/{documentType}")
 public ResponseEntity<byte[]> getDocument(
         @PathVariable Long id,
         @PathVariable String documentType) {

  Optional<TApplication> optionalApp = tApplicationRepository.findById(id);
  if (optionalApp.isEmpty()) {
   return ResponseEntity.notFound().build();
  }

  TApplication application = optionalApp.get();
  byte[] document;
  MediaType contentType;

  switch (documentType) {
   case "passportPhoto":
    document = application.getPassportPhoto();
    contentType = MediaType.IMAGE_JPEG;
    break;
   case "ageProof":
    document = application.getAgeProof();
    contentType = MediaType.APPLICATION_PDF;
    break;
   case "marksheet":
    document = application.getClass10and12Marksheet();
    contentType = MediaType.APPLICATION_PDF;
    break;
   // Add cases for all other document types...
   case "certificate":
    document = application.getClass10and12certificate();
    contentType = MediaType.APPLICATION_PDF;
   case "castecertificate":
    document = application.getCaste_Certificate();
    contentType = MediaType.IMAGE_JPEG;
   case "charactercertificate":
    document = application.getCharacter_Certificate();
    contentType = MediaType.IMAGE_JPEG;
   case "neetresults":
    document = application.getNeet_Results();
    contentType = MediaType.IMAGE_JPEG;
   case "prc":
    document = application.getPrc();
    contentType = MediaType.IMAGE_JPEG;
   case "pwd":
    document = application.getPWD_Certificate();
    contentType = MediaType.IMAGE_JPEG;
   default:
    return ResponseEntity.badRequest().build();
  }

  if (document == null || document.length == 0) {
   return ResponseEntity.notFound().build();
  }

  return ResponseEntity.ok()
          .contentType(contentType)
          .header("Content-Disposition", "inline")
          .body(document);
 }

 @GetMapping("/applications/{id}/full")
 @ResponseBody
 public ResponseEntity<Map<String, Object>> getFullApplicationDetails(
         @PathVariable Long id,
         Authentication authentication) {

  // Check authentication
  if (authentication == null) {
   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  // Get the application
  Optional<TApplication> optionalApp = tApplicationRepository.findById(id);
  if (optionalApp.isEmpty()) {
   return ResponseEntity.notFound().build();
  }

  TApplication application = optionalApp.get();

  // Verify the application belongs to the current user
  String currentUserEmail = authentication.getName();
  if (!application.getUser().getEmail().equals(currentUserEmail)) {
   return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }

  // Build the response
  Map<String, Object> response = new HashMap<>();

  // 1. Basic application info
  response.put("id", application.getA_id());
  response.put("status", application.getStatus() != null ? application.getStatus() : "pending");
  response.put("createdDate", application.getCreatedDate());
  response.put("dob", application.getDOB());
  response.put("gender", application.getGender());
  response.put("nationality", application.getNationality());
  response.put("religion", application.getReligion());

  // 2. Academic info
  response.put("physicsScore", application.getPhysicsScore());
  response.put("chemistryScore", application.getChemistryScore());
  response.put("biologyBiotechScore", application.getBiologyBiotechScore());
  response.put("neetScore", application.getNeetScore());
  response.put("subjectChoice", application.getSubjectChoice());

  // 3. User info
  Map<String, Object> userInfo = new HashMap<>();
  userInfo.put("name", application.getUser().getName());
  userInfo.put("email", application.getUser().getEmail());
  userInfo.put("phone", application.getUser().getPhone());
  response.put("user", userInfo);

  // 4. Course info
  if (application.getmCourse() != null) {
   Map<String, Object> courseInfo = new HashMap<>();
   courseInfo.put("courseId", application.getmCourse().getCourseid());
   courseInfo.put("courseName", application.getmCourse().getCoursename());
   courseInfo.put("institute", application.getmCourse().getInstitute());
   response.put("course", courseInfo);
  }

  // 5. Address info
  if (application.getPermanentAddress() != null) {
   response.put("permanentAddress", mapAddress(application.getPermanentAddress()));
  }
  if (application.getCorrespondenceAddress() != null) {
   response.put("correspondenceAddress", mapAddress(application.getCorrespondenceAddress()));
  }

  // 6. Document URLs
  Map<String, String> documentUrls = new HashMap<>();
  documentUrls.put("passportPhoto", "/application/applications/" + id + "/documents/passportPhoto");
  documentUrls.put("ageProof", "/application/applications/" + id + "/documents/ageProof");
  documentUrls.put("marksheet", "/application/applications/" + id + "/documents/marksheet");
  documentUrls.put("certificate", "/application/applications/" + id + "/documents/certificate");

  if (application.getCaste_Certificate() != null) {
   documentUrls.put("casteCertificate", "/application/applications/" + id + "/documents/casteCertificate");
  }
  if (application.getPWD_Certificate() != null) {
   documentUrls.put("pwdCertificate", "/application/applications/" + id + "/documents/pwdCertificate");
  }
  if (application.getPrc() != null) {
   documentUrls.put("prc", "/application/applications/" + id + "/documents/prc");
  }

  documentUrls.put("neetResults", "/application/applications/" + id + "/documents/neetResults");
  documentUrls.put("characterCertificate", "/application/applications/" + id + "/documents/characterCertificate");

  response.put("documentUrls", documentUrls);

  return ResponseEntity.ok(response);
 }

 @GetMapping("/applications/{id}/download")
 public ResponseEntity<byte[]> downloadApplicationPdf(
         @PathVariable Long id,
         Authentication authentication) throws Exception {

  // Check authentication
  if (authentication == null) {
   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  // Get the application
  Optional<TApplication> optionalApp = tApplicationRepository.findById(id);
  if (optionalApp.isEmpty()) {
   return ResponseEntity.notFound().build();
  }

  TApplication application = optionalApp.get();

  // Verify the application belongs to the current user
  String currentUserEmail = authentication.getName();
  if (!application.getUser().getEmail().equals(currentUserEmail)) {
   return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }

  // Generate PDF
  byte[] pdfBytes = generateApplicationPdf(application);

  return ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_PDF)
          .header("Content-Disposition", "attachment; filename=application_" + id + ".pdf")
          .body(pdfBytes);
 }

 private byte[] generateApplicationPdf(TApplication application) throws Exception {
  try (PDDocument document = new PDDocument()) {
   PDPage page = new PDPage();
   document.addPage(page);

   try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
    // Set up fonts
    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);

    // Add title
    contentStream.beginText();
    contentStream.newLineAtOffset(100, 750);
    contentStream.showText("Application Details");
    contentStream.endText();

    // Set smaller font for content
    contentStream.setFont(PDType1Font.HELVETICA, 12);

    // Add application details
    contentStream.beginText();
    contentStream.newLineAtOffset(100, 700);
    contentStream.showText("Application ID: " + application.getA_id());
    contentStream.newLineAtOffset(0, -20);
    contentStream.showText("Status: " + (application.getStatus() != null ? application.getStatus() : "pending"));
    contentStream.newLineAtOffset(0, -20);
    contentStream.showText("Submitted Date: " +
            (application.getCreatedDate() != null ? application.getCreatedDate().toString() : "N/A"));

    // User information
    contentStream.newLineAtOffset(0, -40);
    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
    contentStream.showText("Applicant Information");
    contentStream.setFont(PDType1Font.HELVETICA, 12);
    contentStream.newLineAtOffset(0, -20);
    contentStream.showText("Name: " + application.getUser().getName());
    contentStream.newLineAtOffset(0, -20);
    contentStream.showText("Email: " + application.getUser().getEmail());
    contentStream.newLineAtOffset(0, -20);
    contentStream.showText("Phone: " + application.getUser().getPhone());

    // Course information
    if (application.getmCourse() != null) {
     contentStream.newLineAtOffset(0, -40);
     contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
     contentStream.showText("Course Information");
     contentStream.setFont(PDType1Font.HELVETICA, 12);
     contentStream.newLineAtOffset(0, -20);
     contentStream.showText("Course: " + application.getmCourse().getCoursename());
     contentStream.newLineAtOffset(0, -20);
     contentStream.showText("Institute: " + application.getmCourse().getInstitute());
    }

    // Academic details
    contentStream.newLineAtOffset(0, -40);
    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
    contentStream.showText("Academic Details");
    contentStream.setFont(PDType1Font.HELVETICA, 12);
    contentStream.newLineAtOffset(0, -20);
    contentStream.showText("Physics Score: " + application.getPhysicsScore());
    contentStream.newLineAtOffset(0, -20);
    contentStream.showText("Chemistry Score: " + application.getChemistryScore());
    contentStream.newLineAtOffset(0, -20);
    contentStream.showText("Biology/Biotech Score: " + application.getBiologyBiotechScore());
    contentStream.newLineAtOffset(0, -20);
    contentStream.showText("NEET Score: " + application.getNeetScore());

    contentStream.endText();
   }

   ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
   document.save(byteArrayOutputStream);
   return byteArrayOutputStream.toByteArray();
  }
 }

 private Map<String, Object> mapAddress(MPostalAddress address) {
  Map<String, Object> addressMap = new HashMap<>();
  addressMap.put("addressLine1", address.getAddressLine1());
  addressMap.put("addressLine2", address.getAddressLine2());
  addressMap.put("district", address.getDistrict());
  addressMap.put("state", address.getState());
  addressMap.put("pincode", address.getPincode());
  return addressMap;
 }
// @GetMapping("/application-details")
// public ModelAndView applicationDetails(@RequestParam Long id, HttpSession session) {
//  // Check if user is authenticated (either admin or regular user)
//  String role = (String) session.getAttribute("userRole");
//  Long sessionUserId = (Long) session.getAttribute("userId");
//
//  // Get the application to check ownership
//  TApplication tApplication = applicationService.getApplicationById(id);
//
//  if (role == null) {
//   // Not logged in - store requested ID and redirect to login
//   session.setAttribute("redirectAppId", id);
//   return new ModelAndView("redirect:/login");
//  }

//  // Check if user is admin OR owner of the application
//  if ("admin".equals(role) || (sessionUserId != null && sessionUserId.equals(tApplication.getUser().getUid()))) {
//   ModelAndView mav = new ModelAndView("application-details");
//   mav.addObject("application", tApplication); // Pass the full application object
//   return mav;
//  }
//
//  // Not authorized
//  return new ModelAndView("redirect:/access-denied");
// }
// @GetMapping("/admin/application-preview")
// public ModelAndView adminApplicationPreview(@RequestParam Long id, HttpSession session) {
//  String role = (String) session.getAttribute("userRole");
//  if (!"admin".equals(role)) {
//   return new ModelAndView("redirect:/admin/login");
//  }
//
//  TApplication tApplication = ApplicationService.getApplicationById(id);
//  ModelAndView mav = new ModelAndView("admin-application-preview");
//  mav.addObject("application", application);
//  mav.addObject("isAdmin", true);
//  return mav;
// }
//
// @PostMapping("/admin/application/{id}/status")
// public ResponseEntity<?> updateApplicationStatus(
//         @PathVariable Long id,
//         @RequestBody StatusUpdateRequest request,
//         HttpSession session) {
//
//  String role = (String) session.getAttribute("userRole");
//  if (!"admin".equals(role)) {
//   return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//  }
//
//  applicationService.updateApplicationStatus(id, request.getStatus(), request.getComments());
//  return ResponseEntity.ok().build();
// }

}


