package com.health.healthdemo.entity;



import jakarta.persistence.*;

@Entity
public class MPostalAddress {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private  String AddressLine1;
    private String AddressLine2;
    @ManyToOne
    private MDistrict District;
    private String State;
    private int Pincode;


    public String getAddressLine1() {
        return AddressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        AddressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return AddressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        AddressLine2 = addressLine2;
    }

    public MDistrict getDistrict() {
        return District;
    }

    public void setDistrict(MDistrict district) {
        District = district;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public int getPincode() {
        return Pincode;
    }

    public void setPincode(int pincode) {
        Pincode = pincode;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
