package com.health.healthdemo.services;

import com.health.healthdemo.entity.MDocuments;
import com.health.healthdemo.repository.MDocumentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentsService {


    @Autowired
    private MDocumentsRepository documentRepository;

    public List<MDocuments> getAllDocuments() {
        return documentRepository.findAll();
    }

    public List<MDocuments> getDocumentsByType(String dtype) {
        return documentRepository.findByDtype(dtype);
    }
}
