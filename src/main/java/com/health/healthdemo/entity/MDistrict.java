package com.health.healthdemo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MDistrict {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("D_id")
    private Long D_id;
    private String districtname;


    public MDistrict() {}

    public Long getD_id() {
        return D_id;
    }

    public void setD_id(Long d_id) {
        D_id = d_id;
    }

    public String getDistrictname() {
        return districtname;
    }

    public void setDistrictname(String districtname) {
        this.districtname = districtname;
    }
}
