package com.health.healthdemo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Courseid;
    private String Coursename;

    public Long getCourseid() {
        return Courseid;
    }

    public void setCourseid(Long courseid) {
        Courseid = courseid;
    }

    public String getCoursename() {
        return Coursename;
    }

    public void setCoursename(String coursename) {
        Coursename = coursename;
    }
}
