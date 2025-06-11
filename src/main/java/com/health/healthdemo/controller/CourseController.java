package com.health.healthdemo.controller;


import com.health.healthdemo.entity.MCourse;
import com.health.healthdemo.repository.MCourseRepository;
import com.health.healthdemo.services.CouncellingService;
import com.health.healthdemo.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private MCourseRepository mCourseRepository;
    private CouncellingService counsellingService;

    @GetMapping("/{Courseid}")
    public ResponseEntity<MCourse> getCourseById(@PathVariable Long Courseid) {
        Optional<MCourse> course = mCourseRepository.findById(Courseid);
        return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public MCourse createCourse(@RequestBody MCourse course) {
        return mCourseRepository.save(course);
    }

    @PutMapping("/{Courseid}")
    public ResponseEntity<MCourse> updateCourse(@PathVariable Long Courseid, @RequestBody MCourse courseDetails) {
        Optional<MCourse> optionalCourse = mCourseRepository.findById(Courseid);
        if (optionalCourse.isPresent()) {
            MCourse course = optionalCourse.get();
            course.setCoursename(courseDetails.getCoursename());
            return ResponseEntity.ok(mCourseRepository.save(course));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{Courseid}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long Courseid) {
        if (mCourseRepository.existsById(Courseid)) {
            mCourseRepository.deleteById(Courseid);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/mbbs-counselling-status")
    public Map<String, Object> getCounsellingStatus() {
        Map<String, Object> response = new HashMap<>();
        LocalDate counsellingDeadline = counsellingService.getCounsellingDeadline(); // Fetch deadline
        LocalDate today = LocalDate.now();

        boolean isActive = today.isBefore(counsellingDeadline);
        response.put("isCounsellingActive", isActive);
        response.put("deadline", counsellingDeadline.toString()); // Convert to string for JSON

        return response;
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        boolean isActive = counsellingService.isCouncellingActive();
        LocalDate deadline = counsellingService.getCounsellingDeadline();

        model.addAttribute("isCounsellingActive", isActive);
        model.addAttribute("counsellingDeadline", deadline);

        return "home";
    }

    @GetMapping("/api/course/details/{id}")
    @ResponseBody
    public ResponseEntity<MCourse> getCourseDetailsById(@PathVariable Long id) {
        Optional<MCourse> course = mCourseRepository.findById(id);
        return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        try {
            List<MCourse> courses = mCourseRepository.findAll();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching courses"));
        }
    }


}



