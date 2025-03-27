package com.health.healthdemo.services;


import com.health.healthdemo.entity.MCourse;
import com.health.healthdemo.repository.MCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
@Autowired
    private MCourseRepository mCourseRepository;
    public List<MCourse> getAllCourses() {
        return mCourseRepository.findAll();
    }

    public Optional<MCourse> getCourseById(Long Courseid) {
        return mCourseRepository.findById(Courseid);
    }
    public MCourse addCourse(MCourse mCourse) {
        return mCourseRepository.save(mCourse);
    }

    public MCourse updateCourse(MCourse mCourse) {
            if(mCourseRepository.existsById(mCourse.getCourseid())) {
                return mCourseRepository.save(mCourse);
        }
        return null;
    }
//    public void deleteById(Long Courseid) {
//        mCourseRepository.deleteById(Courseid);
//    }
}
