package com.health.healthdemo.controller;

import com.health.healthdemo.entity.MDocuments;
import com.health.healthdemo.services.DocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentsController {
    @Autowired
    private DocumentsService documentsService;

    @GetMapping
    public List<MDocuments> getAllDocuments(){
        return documentsService.getAllDocuments();
    }

    @GetMapping("/{dtype}")

    public List<MDocuments> getDocumentsByType(@PathVariable String dtype){
        return documentsService.getDocumentsByType(dtype);
    }
}
