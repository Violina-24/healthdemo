package com.health.healthdemo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MDistrict {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long D_id;
    private String districtname;
    private int districtcode;
    private int LGD_code;

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

    public int getDistrictcode() {
        return districtcode;
    }

    public void setDistrictcode(int districtcode) {
        this.districtcode = districtcode;
    }

    public int getLGD_code() {
        return LGD_code;
    }

    public void setLGD_code(int LGD_code) {
        this.LGD_code = LGD_code;
    }
}
