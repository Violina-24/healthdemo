package com.health.healthdemo.services;

import com.health.healthdemo.entity.*;
import com.health.healthdemo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ApplicationService {
    @Autowired
    private TApplicationRepository tApplicationRepository;

    @Autowired
    private MUsersRepository mUsersRepository;

    @Autowired
    private MCategoryRepository mCategoryRepository;

    @Autowired
    private MPostalAddressRepository mPostalAddressRepository;

    @Autowired
    private MCourseRepository mCourseRepository;

    @Autowired
    private  MDistrictRepository mDistrictRepository;


    public TApplication saveApplication(TApplication tApplication) {
        System.out.println("Start saving TApplication");

        // 1. Validate & fetch existing user
        Long userId = tApplication.getUser() != null ? tApplication.getUser().getUid() : null;
        System.out.println("Fetching User with id: " + userId);
        MUsers user = mUsersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
        System.out.println("Found User: " + user.getName());
        tApplication.setUser(user);

        // 2. Validate & fetch existing category
        Integer categoryId = tApplication.getCid() != null ? tApplication.getCid().getCid() : null;
        System.out.println("Fetching Category with id: " + categoryId);
        MCategory category = mCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id " + categoryId));
        System.out.println("Found Category: " + category.getCategoryname());
        tApplication.setCid(category);

        // 3. Validate & fetch existing course
        Long courseId = tApplication.getmCourse() != null ? tApplication.getmCourse().getCourseid() : null;
        System.out.println("Fetching Course with id: " + courseId);
        MCourse course = mCourseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id " + courseId));
        System.out.println("Found Course: " + course.getCoursename());
        tApplication.setmCourse(course);

        // 4. Create and save new postal address
        MPostalAddress inputAddress = tApplication.getmPostalAddress();
        if (inputAddress == null) {
            throw new RuntimeException("Postal address is required");
        }
        System.out.println("Creating new Postal Address");

        // If District is included by id, fetch from DB
        Long districtId = inputAddress.getDistrict() != null ? inputAddress.getDistrict().getD_id() : null;
        MDistrict district = null;
        if (districtId != null) {
            System.out.println("Fetching District with id: " + districtId);
            district = mDistrictRepository.findById(districtId)
                    .orElseThrow(() -> new RuntimeException("District not found with id " + districtId));
            System.out.println("Found District: " + district.getDistrictname());  // assuming getName() exists
        }

        // Create new MPostalAddress instance to avoid accidentally saving an existing address
        MPostalAddress newAddress = new MPostalAddress();
        newAddress.setAddressLine1(inputAddress.getAddressLine1());
        newAddress.setAddressLine2(inputAddress.getAddressLine2());
        newAddress.setDistrict(district);
        newAddress.setState(inputAddress.getState());
        newAddress.setPincode(inputAddress.getPincode());

        MPostalAddress savedAddress = mPostalAddressRepository.save(newAddress);
        System.out.println("Saved new Postal Address with id: " + savedAddress.getId());

        tApplication.setmPostalAddress(savedAddress);

        // 5. Save the TApplication entity
        TApplication savedApp = tApplicationRepository.save(tApplication);
        System.out.println("Saved TApplication with id: " + savedApp.getA_id());

        System.out.println("Finished saving TApplication");
        return savedApp;
    }


    public Optional<TApplication> getApplicationById(Long A_id) {
        return tApplicationRepository.findById(A_id);
    }

    public List<TApplication> getAllApplications() {
        return tApplicationRepository.findAll();
    }

    public void deleteApplicationById(Long A_id) {
        tApplicationRepository.deleteById(A_id);
    }


}


//    public Long saveApplicationFromForms(Map<String, Object> studentformForm,
//                                         Map<String, Object> quotaForm,
//                                         Map<String, Object> qualificationForm,
//                                         Map<String, Object> fileuploadForm,
//                                         String email) {
//
//        MUsers user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found for email: " + email));
//
//        TApplication application = new TApplication();
//        application.setUser(user);
//
//        // --- Student Form Data ---
//        try {
//            String dobStr = (String) studentformForm.get("dob");
//            if (dobStr == null) throw new RuntimeException("Date of Birth is missing");
//            application.setDOB(LocalDate.parse(dobStr));
//            application.setAge(Period.between(application.getDOB(), LocalDate.now()).getYears());
//
//            application.setNationality((String) studentformForm.getOrDefault("nationality", ""));
//            application.setReligion((String) studentformForm.getOrDefault("religion", ""));
//            application.setGender((String) studentformForm.getOrDefault("gender", ""));
//        } catch (Exception e) {
//            throw new RuntimeException("Error in Student Form Data: " + e.getMessage());
//        }
//
//        // --- Quota Data ---
//        try {
//            String categoryStr = (String) quotaForm.get("category");
//            if (categoryStr == null) throw new RuntimeException("Category is missing");
//            int cid = Integer.parseInt(categoryStr);
//
//            List<MCategory> categories = categoryRepository.findByCid(cid);
//            if (categories.isEmpty()) throw new RuntimeException("Invalid category ID: " + cid);
//            application.setCid(categories.get(0));
//
//            // Optional: Handle institute/course if needed
//            // Integer instituteId = Integer.parseInt(quotaForm.get("institute").toString());
//            // MCourse institute = mCourseRepository.findById(instituteId)
//            //         .orElseThrow(() -> new RuntimeException("Invalid institute"));
//            // application.setInstitute(institute);
//        } catch (Exception e) {
//            throw new RuntimeException("Error in Quota Form Data: " + e.getMessage());
//        }
//
//        // --- Qualification Data ---
//        try {
//            // Extract values safely from map with null checks
//            Object physicsObj = qualificationForm.get("physics");
//            Object chemistryObj = qualificationForm.get("chemistry");
//            Object biologyObj = qualificationForm.get("biology");
//            Object subjectChoiceObj = qualificationForm.get("subjectChoice");
//            Object neetObj = qualificationForm.get("neet");
//
//            if (physicsObj == null) throw new RuntimeException("Physics score is missing");
//            if (chemistryObj == null) throw new RuntimeException("Chemistry score is missing");
//            if (biologyObj == null) throw new RuntimeException("Biology score is missing");
//            if (subjectChoiceObj == null) throw new RuntimeException("Subject choice is missing");
//            if (neetObj == null) throw new RuntimeException("NEET score is missing");
//
//            float physicsScore = Float.parseFloat(physicsObj.toString());
//            float chemistryScore = Float.parseFloat(chemistryObj.toString());
//            float biologyScore = Float.parseFloat(biologyObj.toString());
//            int neetScore = Integer.parseInt(neetObj.toString());
//            String subjectChoiceStr = subjectChoiceObj.toString();
//
//            // You can calculate PCB marks as sum or whatever logic you want
//            int pcbMarks = Math.round(physicsScore + chemistryScore + biologyScore);
//
//            application.setPhysics_Score(physicsScore);
//            application.setChemistry_Score(chemistryScore);
//            application.setBiology_Biotech_Score(biologyScore);
//            application.setNEET_SCORE(neetScore);
//            application.setPCB_Marks(pcbMarks);
//
//            // Convert string to enum safely, if your enum values match exactly
//            application.setSubjectChoice(subjectChoiceStr);
//
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error in Qualification Form Data: " + e.getMessage());
//        }


// Assume you have a districtRepository injected (MDistrictRepository)

//        try {
//            System.out.println("Permanent Address Form Data: " + studentformForm);
//
//            // Extract the nested map for permanent address
//            Map<String, Object> permAddrMap = (Map<String, Object>) studentformForm.get("permanent_Address");
//            System.out.println("Permanent Address Map: " + permAddrMap);
//
//            Object permPincodeObj = permAddrMap.get("pincode");
//            System.out.println("permPincode value: " + permPincodeObj);
//
//            MPostalAddress permanentAddress = new MPostalAddress();
//            permanentAddress.setAddressLine1((String) permAddrMap.get("addressLine1"));
//            permanentAddress.setAddressLine2((String) permAddrMap.get("addressLine2"));
//            permanentAddress.setState((String) permAddrMap.get("state"));
//            permanentAddress.setPincode(permPincodeObj != null ? Integer.parseInt(permPincodeObj.toString()) : 0);
//
//            String permDistrictName = (String) permAddrMap.get("district");
//            System.out.println("permDistrictName: " + permDistrictName);
//
//            MDistrict permDistrict = districtRepository.findByDistrictname(permDistrictName)
//                    .orElseThrow(() -> new RuntimeException("Permanent District not found: " + permDistrictName));
//            permanentAddress.setDistrict(permDistrict);

//            addressRepository.save(permanentAddress);
//            application.setPermanent_Address(permanentAddress);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Error in Permanent Address: " + e.getMessage());
//        }



//        try {
//            MPostalAddress correspondenceAddress = new MPostalAddress();
//            correspondenceAddress.setAddressLine1((String) studentformForm.get("corrAddressLine1"));
//            correspondenceAddress.setAddressLine2((String) studentformForm.get("corrAddressLine2"));
//            correspondenceAddress.setState((String) studentformForm.get("corrState"));
//            correspondenceAddress.setPincode(Integer.parseInt(studentformForm.get("corrPincode").toString()));
//
//            // Fetch MDistrict entity from DB by district name
//            String corrDistrictName = (String) studentformForm.get("corrDistrict");
//            MDistrict corrDistrict = districtRepository.findByDistrictname(corrDistrictName)
//                    .orElseThrow(() -> new RuntimeException("Correspondence District not found: " + corrDistrictName));
//            correspondenceAddress.setDistrict(corrDistrict);

//            addressRepository.save(correspondenceAddress);
//            application.setCorrespondence_Address(correspondenceAddress);
//        } catch (Exception e) {
//            throw new RuntimeException("Error in Correspondence Address: " + e.getMessage());
//        }


// --- File Uploads ---
//        try {
//            application.setPassportPhoto((byte[]) fileuploadForm.get("passportPhoto"));
//            application.setAgeProof((byte[]) fileuploadForm.get("ageProof"));
//            application.setClass10and12Marksheet((byte[]) fileuploadForm.get("Class10and12Marksheet"));
//            application.setClass10and12certificate((byte[]) fileuploadForm.get("class10and12certificate"));
//            application.setNeet_Results((byte[]) fileuploadForm.get("neetResult"));
//            application.setPrc((byte[]) fileuploadForm.get("prc"));
//            application.setCharacter_Certificate((byte[]) fileuploadForm.get("characterCertificate"));
//            application.setCaste_Certificate((byte[]) fileuploadForm.get("casteCertificate"));
//            application.setPWD_Certificate((byte[]) fileuploadForm.get("pwdCertificate"));
//        } catch (Exception e) {
//            throw new RuntimeException("Error in File Uploads: " + e.getMessage());
//        }
//
//        // --- Save & Return ---
//        TApplication savedApp = applicationRepository.save(application);
//        return savedApp.getA_id();
