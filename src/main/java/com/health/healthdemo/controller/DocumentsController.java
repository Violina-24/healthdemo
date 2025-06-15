package com.health.healthdemo.controller;

import com.health.healthdemo.entity.MCourse;
import com.health.healthdemo.entity.MDocuments;
import com.health.healthdemo.repository.MCourseRepository;
import com.health.healthdemo.services.DocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/documents")
public class DocumentsController {
    @Autowired
    private DocumentsService documentsService;

    @Autowired
    private MCourseRepository courseRepository;

    @GetMapping
    public List<MDocuments> getAllDocuments(){
        return documentsService.getAllDocuments();
    }

    @GetMapping("/{dtype}")
    public List<Map<String, Object>> getDocumentsByType(@PathVariable String dtype) {
        List<MDocuments> docs = documentsService.getDocumentsByType(dtype);
        List<Map<String, Object>> result = new ArrayList<>();

        for (MDocuments doc : docs) {
            Map<String, Object> map = new HashMap<>();
            map.put("dno", doc.getDNo());  // Match frontend expected property names
            map.put("ddesc", doc.getDDesc());
            map.put("ddate", doc.getDDate());
            map.put("dfile", doc.getDFile());
            map.put("opendate", doc.getOpendate());
            map.put("enddate", doc.getEnddate());

            // Add course info if needed by frontend
            if (doc.getCourse() != null) {
                map.put("course", doc.getCourse().getCoursename());
            }

            result.add(map);
        }
        return result;
    }

    @GetMapping("/with-course/{dtype}")
    public List<Map<String, Object>> getDocumentsWithCourse(@PathVariable String dtype) {
        List<MDocuments> docs = documentsService.getDocumentsByType(dtype);
        List<Map<String, Object>> result = new ArrayList<>();

        for (MDocuments doc : docs) {
            Map<String, Object> map = new HashMap<>();
            map.put("dno", doc.getDNo());
            map.put("ddesc", doc.getDDesc());
            map.put("ddate", doc.getDDate());
            map.put("dfile", doc.getDFile());
            map.put("opendate", doc.getOpendate());
            map.put("enddate", doc.getEnddate());

            MCourse course = doc.getCourse();
            if (course != null) {
                map.put("courseid", course.getCourseid());
                map.put("courseName", course.getCoursename());
                map.put("institute", course.getInstitute());
            } else {
                map.put("courseid", null);
                map.put("courseName", "Unknown");
                map.put("institute", "Unknown");
            }

            result.add(map);
        }

        return result;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadAdvertisement(
            @RequestParam("dtype") String dtype,
            @RequestParam("dNo") String dNo,
            @RequestParam("dDate") String dDate,
            @RequestParam("dDesc") String dDesc,
            @RequestParam("opendate") String opendate,
            @RequestParam("enddate") String enddate,
            @RequestParam(value = "courseid", required = false) Long courseId,
            @RequestParam("file") MultipartFile file) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            MDocuments document = new MDocuments();
            document.setDtype(dtype);
            document.setDNo(dNo);
            document.setDDate(dateFormat.parse(dDate));
            document.setDDesc(dDesc);
            document.setOpendate(dateFormat.parse(opendate));
            document.setEnddate(dateFormat.parse(enddate));

            // Set course if provided
            if (courseId != null) {
                MCourse course = courseRepository.findById(courseId)
                        .orElseThrow(() -> new RuntimeException("Course not found"));
                document.setCourse(course);
            }

            // Save the file and get the path
            String filePath = documentsService.storeFile(file);
            document.setDFile(filePath);

            MDocuments savedDocument = documentsService.saveDocument(document);
            return ResponseEntity.ok(savedDocument);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error uploading document: " + e.getMessage());
        }
    }

}
