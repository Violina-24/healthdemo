package com.health.healthdemo.entity;

import jakarta.persistence.*;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"Uid", "Courseid"})
)
public class MPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Pid;

    @ManyToOne
    @JoinColumn(name = "uid", nullable = false)
    private MUsers mUsers;

    @ManyToOne
    @JoinColumn(name = "Courseid", nullable = false)
    private MCourse mCourse;

    private int preference;

    public Long getPid() {
        return Pid;
    }

    public void setPid(Long pid) {
        Pid = pid;
    }

    public MUsers getmUsers() {
        return mUsers;
    }

    public void setmUsers(MUsers mUsers) {
        this.mUsers = mUsers;
    }

    public MCourse getmCourse() {
        return mCourse;
    }

    public void setmCourse(MCourse mCourse) {
        this.mCourse = mCourse;
    }

    public int getPreference() {
        return preference;
    }

    public void setPreference(int preference) {
        this.preference = preference;
    }
}