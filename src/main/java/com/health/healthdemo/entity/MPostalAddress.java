package com.health.healthdemo.entity;



import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;

@Entity
public class MPostalAddress {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(name = "address_line1")  // Match database column
    private String addressLine1;  // Change to lowercase

    @Column(name = "address_line2")
    private String addressLine2;

    @ManyToOne
    @JoinColumn(name = "d_id")// Match your actual DB column
    private MDistrict district;

//    @Transient
//    private Long districtId;

    private String state;
    private int pincode;

//    public Long getDistrictId() {
//        return districtId;
//    }
//
//    public void setDistrictId(Long districtId) {
//        this.districtId = districtId;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public MDistrict getDistrict() {
        return district;
    }

    public void setDistrict(MDistrict district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }
}
