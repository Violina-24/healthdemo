package com.health.healthdemo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.health.healthdemo.Base64ToByteArrayDeserializer;
import jakarta.persistence.*;

import java.time.LocalDate;



@Entity


public class TApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long A_id;

    private LocalDate DOB;

    private int age;

    private String gender;

    private String religion;

    private String nationality;

    @Column(name = "physics_score")
    @JsonProperty("physicsScore")
    private Float physicsScore;

    @Column(name = "chemistry_score")
    @JsonProperty("chemistryScore")
    private Float chemistryScore;

    @Column(name = "biology_biotech_score")
    @JsonProperty("biologyBiotechScore")
    private Float biologyBiotechScore;

    @Column(name = "pcb_marks")
    @JsonProperty("pcbMarks")
    private int pcbMarks;

    @Column(name = "neet_score")
    @JsonProperty("neetScore")
    private int neetScore;

    @Column(name = "subject_choice")
    @JsonProperty("subjectChoice")
    private String subjectChoice;

    @Column(name = "parents_guardian_name")
    @JsonProperty("parentsGuardianName")
    private String parentsGuardianName;


    @Column(name = "management_quota")
    @JsonProperty("management_quota") // Explicit JSON property name
    private Boolean managementQuota;

    @Column(name = "physically_challenged")
    @JsonProperty("physically_challenged")
    private Boolean physicallyChallenged;

    @Column(name = "economically_weaker_section")
    @JsonProperty("economically_weaker_section")
    private Boolean ews;


    @Lob
    @JsonDeserialize(using = Base64ToByteArrayDeserializer.class)
    private byte[] PassportPhoto;

    @Lob
    @JsonDeserialize(using = Base64ToByteArrayDeserializer.class)
    private byte[] AgeProof;

    @Lob
    @JsonDeserialize(using = Base64ToByteArrayDeserializer.class)
    private byte[] class10and12Marksheet;

    @Lob
    @JsonDeserialize(using = Base64ToByteArrayDeserializer.class)
    private byte[] class10and12certificate;

    @Lob
    @JsonProperty("casteCertificate")
    @JsonDeserialize(using = Base64ToByteArrayDeserializer.class)
    private byte[] Caste_Certificate;

    @Lob
    @JsonDeserialize(using = Base64ToByteArrayDeserializer.class)
    private byte[] Prc;

    @Lob
    @JsonProperty("neetResult")
    @JsonDeserialize(using = Base64ToByteArrayDeserializer.class)
    private byte[] Neet_Results;
    @Lob
    @JsonProperty("characterCertificate")
    @JsonDeserialize(using = Base64ToByteArrayDeserializer.class)
    private byte[] Character_Certificate;

    @Lob
    @JsonProperty("pwdCertificate")
    @JsonDeserialize(using = Base64ToByteArrayDeserializer.class)
    private byte[] PWD_Certificate;

    @ManyToOne
    @JoinColumn(name="uid", nullable = false)
    private MUsers user;

    @ManyToOne
    @JoinColumn(name = "cid")
    private MCategory cid;

//    @Transient
//    private Long McategoryId;

//    @ManyToOne
//    @JoinColumn(name = "id")
//    private MPostalAddress mPostalAddress;

    @ManyToOne
    @JoinColumn(name = "permanent_address_id")
    private MPostalAddress permanentAddress;

    @ManyToOne
    @JoinColumn(name = "correspondence_address_id")
    @JsonProperty("correspondenceAddress") // Add this annotation
    private MPostalAddress correspondenceAddress; // Standardize to camelCase

// need the coursename too to check which course the student has applied

    // ManyToOne relationship to Course (institute)
    @ManyToOne
    @JoinColumn(name = "Courseid")
    private MCourse mCourse;

    @PreUpdate
    private void  validateNEET_SCORERange() {
        if (neetScore < -180 || neetScore > 720) {
            throw  new  IllegalArgumentException("NEET_SCORE must be in valid range ");
        }
    }

    @Column(name = "status")
    private String status;

//    private boolean Disability = false;
//    public TApplication() {
//        this.Disability = false; // by default false
//    }

    public Long getA_id() {
        return A_id;
    }

    public void setA_id(Long a_id) {
        A_id = a_id;
    }

    public LocalDate getDOB() {
        return DOB;
    }

    public void setDOB(LocalDate DOB) {
        this.DOB = DOB;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Float getPhysicsScore() {
        return physicsScore;
    }

    public void setPhysicsScore(Float physicsScore) {
        this.physicsScore = physicsScore;
    }

    public Float getChemistryScore() {
        return chemistryScore;
    }

    public void setChemistryScore(Float chemistryScore) {
        this.chemistryScore = chemistryScore;
    }

    public Float getBiologyBiotechScore() {
        return biologyBiotechScore;
    }

    public void setBiologyBiotechScore(Float biologyBiotechScore) {
        this.biologyBiotechScore = biologyBiotechScore;
    }

    public int getPcbMarks() {
        return pcbMarks;
    }

    public void setPcbMarks(int pcbMarks) {
        this.pcbMarks = pcbMarks;
    }

    public int getNeetScore() {
        return neetScore;
    }

    public void setNeetScore(int neetScore) {
        this.neetScore = neetScore;
    }

    public String getSubjectChoice() {
        return subjectChoice;
    }

    public void setSubjectChoice(String subjectChoice) {
        this.subjectChoice = subjectChoice;
    }

    public String getParentsGuardianName() {
        return parentsGuardianName;
    }

    public void setParentsGuardianName(String parentsGuardianName) {
        this.parentsGuardianName = parentsGuardianName;
    }

    public Boolean getManagementQuota() {
        return managementQuota;
    }

    public void setManagementQuota(Boolean managementQuota) {
        this.managementQuota = managementQuota;
    }

    public Boolean getPhysicallyChallenged() {
        return physicallyChallenged;
    }

    public void setPhysicallyChallenged(Boolean physicallyChallenged) {
        this.physicallyChallenged = physicallyChallenged;
    }

    public Boolean getEws() {
        return ews;
    }

    public void setEws(Boolean ews) {
        this.ews = ews;
    }

    public byte[] getPassportPhoto() {
        return PassportPhoto;
    }

    public void setPassportPhoto(byte[] passportPhoto) {
        PassportPhoto = passportPhoto;
    }

    public byte[] getAgeProof() {
        return AgeProof;
    }

    public void setAgeProof(byte[] ageProof) {
        AgeProof = ageProof;
    }

    public byte[] getClass10and12Marksheet() {
        return class10and12Marksheet;
    }

    public void setClass10and12Marksheet(byte[] class10and12Marksheet) {
        this.class10and12Marksheet = class10and12Marksheet;
    }

    public byte[] getClass10and12certificate() {
        return class10and12certificate;
    }

    public void setClass10and12certificate(byte[] class10and12certificate) {
        this.class10and12certificate = class10and12certificate;
    }

    public byte[] getCaste_Certificate() {
        return Caste_Certificate;
    }

    public void setCaste_Certificate(byte[] caste_Certificate) {
        Caste_Certificate = caste_Certificate;
    }

    public byte[] getPrc() {
        return Prc;
    }

    public void setPrc(byte[] prc) {
        Prc = prc;
    }


    public byte[] getNeet_Results() {
        return Neet_Results;
    }

    public void setNeet_Results(byte[] neet_Results) {
        Neet_Results = neet_Results;
    }

    public byte[] getCharacter_Certificate() {
        return Character_Certificate;
    }

    public void setCharacter_Certificate(byte[] character_Certificate) {
        Character_Certificate = character_Certificate;
    }

    public byte[] getPWD_Certificate() {
        return PWD_Certificate;
    }

    public void setPWD_Certificate(byte[] PWD_Certificate) {
        this.PWD_Certificate = PWD_Certificate;
    }

    public MUsers getUser() {
        return user;
    }

    public void setUser(MUsers user) {
        this.user = user;
    }

    public MCategory getCid() {
        return cid;
    }

    public void setCid(MCategory cid) {
        this.cid = cid;
    }



//    public MPostalAddress getmPostalAddress() {
//        return mPostalAddress;
//    }
//
//    public void setmPostalAddress(MPostalAddress mPostalAddress) {
//        this.mPostalAddress = mPostalAddress;
//    }

    public MPostalAddress getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(MPostalAddress permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public MPostalAddress getCorrespondenceAddress() {
        return correspondenceAddress;
    }

    public void setCorrespondenceAddress(MPostalAddress correspondenceAddress) {
        this.correspondenceAddress = correspondenceAddress;
    }

    public MCourse getmCourse() {
        return mCourse;
    }

    public void setmCourse(MCourse mCourse) {
        this.mCourse = mCourse;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}





