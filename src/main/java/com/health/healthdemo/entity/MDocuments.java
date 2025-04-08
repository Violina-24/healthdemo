package com.health.healthdemo.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "MDocuments")
public class MDocuments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Did;

    private String dtype;
    private String dDesc;
    private String dNo;
    private Date dDate;
    private String dFile;
    private Date opendate;
    private Date enddate;

    @ManyToOne
    @JoinColumn(name = "courseid")
    private MCourse course;

    // Getters and Setters

    public Long getDid() {
        return Did;
    }

    public void setDid(Long did) {
        Did = did;
    }

    public String getDtype() {
            return dtype;
        }

        public void setDtype(String dtype) {
            this.dtype = dtype;
        }

        public String getDDesc() {
            return dDesc;
        }

        public void setDDesc(String dDesc) {
            this.dDesc = dDesc;
        }

        public String getDNo() {
            return dNo;
        }

        public void setDNo(String dNo) {
            this.dNo = dNo;
        }

        public Date getDDate() {
            return dDate;
        }

        public void setDDate(Date dDate) {
            this.dDate = dDate;
        }

        public String getDFile() {
            return dFile;
        }

        public void setDFile(String dFile) {
            this.dFile = dFile;
        }

         public Date getOpendate() {
        return opendate;
        }

        public void setOpendate(Date opendate) {
        this.opendate = opendate;
        }

        public Date getEnddate() {
        return enddate;
        }

        public void setEnddate(Date enddate) {
        this.enddate = enddate;
        }

        public void setCourse(MCourse course) {
        this.course = course;
        }

        public MCourse getCourse() {
        return course;
        }

}

