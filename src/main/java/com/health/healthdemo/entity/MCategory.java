package com.health.healthdemo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mcategory")
public class MCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cid;

    @Column(name = "category_name") // FIX: Explicitly map to DB column
    private String categoryname;

    public int getCid() { return cid; }
    public void setCid(int cid) { this.cid = cid; }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }
}



