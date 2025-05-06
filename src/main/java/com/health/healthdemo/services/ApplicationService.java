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
    private TApplicationRepository applicationRepository;

    @Autowired
    private MUsersRepository userRepository;

    @Autowired
    private MCategoryRepository categoryRepository;

    @Autowired
    private MPostalAddressRepository addressRepository;

    public TApplication saveApplication(TApplication application) {
        return applicationRepository.save(application);
    }

    public Optional<TApplication> getApplicationById(Long A_id) {
        return applicationRepository.findById(A_id);
    }

    public List<TApplication> getAllApplications() {
        return applicationRepository.findAll();
    }

    public void deleteApplicationById(Long A_id) {
        applicationRepository.deleteById(A_id);
    }

    public void saveApplicationFromForms(Map<String, Object> studentformForm,
                                         Map<String, Object> quotaForm,
                                         Map<String, Object> qualificationForm,
                                         Map<String, Object> fileuploadForm,
                                         String email) {

        MUsers user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TApplication application = new TApplication();

        application.setUser(user);
        application.setDOB(LocalDate.parse((String) studentformForm.get("dob")));


        Period age = Period.between(application.getDOB(), LocalDate.now());
        application.setAge(age.getYears());

        String categoryName = (String) quotaForm.get("category");
        List<MCategory> categories = categoryRepository.findByCategoryname(categoryName);
        if (categories.isEmpty()) {
            throw new RuntimeException("Invalid category: " + categoryName);
        }
        MCategory category = categories.get(0); // use the first one

        application.setCategoryname(category);

        application.setNationality((int) studentformForm.get("nationality"));
        application.setReligion((String) studentformForm.get("religion"));
        application.setGender((String) studentformForm.get("gender"));

        MPostalAddress permanentAddress = new MPostalAddress();
        permanentAddress.setAddressLine1((String) studentformForm.get("permAddressLine1"));
        permanentAddress.setAddressLine2((String) studentformForm.get("permAddressLine2"));
        permanentAddress.setState((String) studentformForm.get("permState"));
        permanentAddress.setPincode((int) studentformForm.get("permPincode"));
        addressRepository.save(permanentAddress);
        application.setPermanent_Address(permanentAddress);

        MPostalAddress correspondenceAddress = new MPostalAddress();
        correspondenceAddress.setAddressLine1((String) studentformForm.get("corrAddressLine1"));
        correspondenceAddress.setAddressLine2((String) studentformForm.get("corrAddressLine2"));
        correspondenceAddress.setState((String) studentformForm.get("corrState"));
        correspondenceAddress.setPincode((int) studentformForm.get("corrPincode"));
        addressRepository.save(correspondenceAddress);
        application.setCorrespondence_Address(correspondenceAddress);

        application.setPassportPhoto((byte[]) fileuploadForm.get("passportPhoto"));
        application.setAgeProof((byte[]) fileuploadForm.get("ageProof"));
        application.setClassXIIMarksheet((byte[]) fileuploadForm.get("ClassXIIMarksheet"));
//        application.setCertificates((String) fileuploadForm.get("class10and12certificatePath"));
        application.setNeet_Results((byte[]) fileuploadForm.get("neetResult"));
        application.setPrc((byte[]) fileuploadForm.get("prc"));
        application.setCharacter_Certificate((byte[]) fileuploadForm.get("characterCertificate"));
        application.setCaste_Certificate((byte[]) fileuploadForm.get("casteCertificate"));
        application.setPWD_Certificate((byte[]) fileuploadForm.get("pwdCertificate"));


        applicationRepository.save(application);
    }
}
