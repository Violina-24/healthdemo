package com.health.healthdemo.controller;

import com.health.healthdemo.entity.MCourse;
import com.health.healthdemo.entity.MDocuments;
import com.health.healthdemo.repository.MCourseRepository;
import com.health.healthdemo.services.DocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public List<MDocuments> getDocumentsByType(@PathVariable String dtype){
        return documentsService.getDocumentsByType(dtype);
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

}
