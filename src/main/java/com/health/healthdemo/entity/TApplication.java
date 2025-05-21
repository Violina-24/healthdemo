package com.health.healthdemo.entity;

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

    private int PCB_Marks;

    private int NEET_SCORE;

    private Float Physics_Score;

    private Float Chemistry_Score;

    private Float Biology_Biotech_Score;

    private String Parents_Guardian_Name;

    private String SubjectChoice;

    @Lob
    private byte[] PassportPhoto;

    @Lob
    private byte[] AgeProof;

    @Lob
    private byte[] class10and12Marksheet;

    @Lob
    private byte[] class10and12certificate;

    @Lob
    private byte[] Caste_Certificate;

    @Lob
    private byte[] Prc;



    @Lob
    private byte[] Neet_Results;
    @Lob
    private byte[] Character_Certificate;
    @Lob
    private byte[] PWD_Certificate;

    @ManyToOne
    @JoinColumn(name="uid", nullable = false)
    private MUsers user;

    @ManyToOne
    @JoinColumn(name = "cid")
    private MCategory cid;

//    @Transient
//    private Long McategoryId;

    @ManyToOne
    @JoinColumn(name = "id")
    private MPostalAddress mPostalAddress;

//    @ManyToOne
//    @JoinColumn(name = "correspondence_address_id")
//    private MPostalAddress Correspondence_Address;

// need the coursename too to check which course the student has applied

    // ManyToOne relationship to Course (institute)
    @ManyToOne
    @JoinColumn(name = "Courseid")
    private MCourse mCourse;

    @PreUpdate
    private void  validateNEET_SCORERange() {
        if (NEET_SCORE < -180 || NEET_SCORE > 720) {
            throw  new  IllegalArgumentException("NEET_SCORE must be in valid range ");
        }
    }


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

    public int getPCB_Marks() {
        return PCB_Marks;
    }

    public void setPCB_Marks(int PCB_Marks) {
        this.PCB_Marks = PCB_Marks;
    }

    public int getNEET_SCORE() {
        return NEET_SCORE;
    }

    public void setNEET_SCORE(int NEET_SCORE) {
        this.NEET_SCORE = NEET_SCORE;
    }

    public Float getPhysics_Score() {
        return Physics_Score;
    }

    public void setPhysics_Score(Float physics_Score) {
        Physics_Score = physics_Score;
    }

    public Float getChemistry_Score() {
        return Chemistry_Score;
    }

    public void setChemistry_Score(Float chemistry_Score) {
        Chemistry_Score = chemistry_Score;
    }

    public Float getBiology_Biotech_Score() {
        return Biology_Biotech_Score;
    }

    public void setBiology_Biotech_Score(Float biology_Biotech_Score) {
        Biology_Biotech_Score = biology_Biotech_Score;
    }

    public String getSubjectChoice() {
        return SubjectChoice;
    }

    public void setSubjectChoice(String subjectChoice) {
        this.SubjectChoice = subjectChoice;
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



    public MPostalAddress getmPostalAddress() {
        return mPostalAddress;
    }

    public void setmPostalAddress(MPostalAddress mPostalAddress) {
        this.mPostalAddress = mPostalAddress;
    }

    public MCourse getmCourse() {
        return mCourse;
    }

    public void setmCourse(MCourse mCourse) {
        this.mCourse = mCourse;
    }

    public String getParents_Guardian_Name() {
        return Parents_Guardian_Name;
    }

    public void setParents_Guardian_Name(String parents_Guardian_Name) {
        Parents_Guardian_Name = parents_Guardian_Name;
    }

}





