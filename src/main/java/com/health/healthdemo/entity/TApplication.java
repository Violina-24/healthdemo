package com.health.healthdemo.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;


@Entity


public class TApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long A_id;
    @ManyToOne
    @JoinColumn(name="uid", nullable = false)
    private MUsers user;
    @ManyToOne
    @JoinColumn(name = "name")
    private MUsers name;

    private LocalDate DOB;
    private int age;
    private String gender;
    @ManyToOne
    @JoinColumn(name = "category_name")
    private MCategory categoryname;

    @ManyToOne
   @JoinColumn(name = "permanent_address_id")
    private MPostalAddress Permanent_Address;

    @ManyToOne
    @JoinColumn(name = "correspondence_address_id")
    private MPostalAddress Correspondence_Address;

    private String religion;
    private int nationality;
    private int PCB_Marks;
    private int NEET_SCORE;


    @PreUpdate
    private void  validateNEET_SCORERange() {
        if (NEET_SCORE < -180 || NEET_SCORE > 720) {
            throw  new  IllegalArgumentException("NEET_SCORE must be in valid range ");
        }
    }
    private String Parents_Guardian_Name;
    private String Parents_Guardian_Occupation_Details;
    private String Parents_Guardian_Name_of_office;
    private String getParents_Guardian_Occupation_Office_Address;
    private boolean Disability = false;
    public TApplication() {
        this.Disability = false; // by default false
    }




    private Float Physics_Score;
    private Float Chemistry_Score;
    private Float Biology_Biotech_Score;
    @Enumerated(EnumType.STRING)
    private SubjectChoice subjectChoice;


    // ManyToOne relationship to Course (institute)
    @ManyToOne
    @JoinColumn(name = "institute") // or your actual column name
    private MCourse institute;

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
    private byte[] Employer_Certificate;
    @Lob
    private byte[] Neet_Results;
    @Lob
    private byte[] Character_Certificate;
    @Lob
    private byte[] PWD_Certificate;

    public MPostalAddress getPermanent_Address() {
        return Permanent_Address;
    }

    public void setPermanent_Address(MPostalAddress permanent_Address) {
        Permanent_Address = permanent_Address;
    }

    public Long getA_id() {
        return A_id;
    }

    public void setA_id(Long a_id) {
        A_id = a_id;
    }

    public MUsers getUser() {
        return user;
    }

    public void setUser(MUsers user) {
        this.user = user;
    }

    public MUsers getName() {
        return name;
    }

    public void setName(MUsers name) {
        this.name = name;
    }

    public MPostalAddress getCorrespondence_Address() {
        return Correspondence_Address;
    }

    public void setCorrespondence_Address(MPostalAddress correspondence_Address) {
        Correspondence_Address = correspondence_Address;
    }

    public SubjectChoice getSubjectChoice() {
        return subjectChoice;
    }

    public void setSubjectChoice(SubjectChoice subjectChoice) {
        this.subjectChoice = subjectChoice;
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

    public MCourse getInstitute() {
        return institute;
    }

    public void setInstitute(MCourse institute) {
        this.institute = institute;
    }

    public MCategory getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(MCategory categoryname) {
        this.categoryname = categoryname;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public int getNationality() {
        return nationality;
    }

    public void setNationality(int nationality) {
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

    public String getParents_Guardian_Name() {
        return Parents_Guardian_Name;
    }

    public void setParents_Guardian_Name(String parents_Guardian_Name) {
        Parents_Guardian_Name = parents_Guardian_Name;
    }

    @PrePersist
    public void calculateAge() {
        this.age = Period.between(DOB, LocalDate.now()).getYears();
    }
    public enum SubjectChoice {
        Biology,Biotechnology
    }
    public String getParents_Guardian_Occupation_Details() {
        return Parents_Guardian_Occupation_Details;
    }

    public void setParents_Guardian_Occupation_Details(String parents_Guardian_Occupation_Details) {
        Parents_Guardian_Occupation_Details = parents_Guardian_Occupation_Details;
    }

    public String getParents_Guardian_Name_of_office() {
        return Parents_Guardian_Name_of_office;
    }

    public void setParents_Guardian_Name_of_office(String parents_Guardian_Name_of_office) {
        Parents_Guardian_Name_of_office = parents_Guardian_Name_of_office;
    }

    public String getGetParents_Guardian_Occupation_Office_Address() {
        return getParents_Guardian_Occupation_Office_Address;
    }

    public void setGetParents_Guardian_Occupation_Office_Address(String getParents_Guardian_Occupation_Office_Address) {
        this.getParents_Guardian_Occupation_Office_Address = getParents_Guardian_Occupation_Office_Address;
    }

    public boolean isDisability() {
        return Disability;
    }

    public void setDisability(boolean disability) {
        Disability = disability;
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
        class10and12Marksheet = class10and12Marksheet;
    }

    public byte[] getCaste_Certificate() {
        return Caste_Certificate;
    }

    public byte[] getClass10and12certificate() {
        return class10and12certificate;
    }

    public void setClass10and12certificate(byte[] class10and12certificate) {
        this.class10and12certificate = class10and12certificate;
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

    public byte[] getEmployer_Certificate() {
        return Employer_Certificate;
    }

    public void setEmployer_Certificate(byte[] employer_Certificate) {
        Employer_Certificate = employer_Certificate;
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
}





