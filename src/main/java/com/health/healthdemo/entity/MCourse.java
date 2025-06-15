package com.health.healthdemo.entity;

import jakarta.persistence.*;

@Entity
public class MCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "courseid")
    private Long Courseid;

    private String Coursename;

    private String institute;

    @Column(nullable = false)
    private int seats;

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

    public String getInstitute() {
        return institute;
    }


    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}
