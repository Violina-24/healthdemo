package com.health.healthdemo.repository;

import com.health.healthdemo.entity.MCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MCourseRepository extends JpaRepository <MCourse,Long> {
//    Long Courseid(Long Courseid);
}
