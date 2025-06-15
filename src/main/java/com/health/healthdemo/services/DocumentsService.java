package com.health.healthdemo.services;

import com.health.healthdemo.entity.MDocuments;
import com.health.healthdemo.repository.MDocumentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentsService {
    @Value("${file.upload-dir}")
    private String uploadDir;


    @Autowired
    private MDocumentsRepository documentsRepository;
    public DocumentsService(MDocumentsRepository documentRepository) {
        this.documentsRepository = documentRepository;
    }


    public List<MDocuments> getAllDocuments() {
        return documentsRepository.findAll();
    }

    public List<MDocuments> getDocumentsByType(String dtype) {
        return documentsRepository.findByDtype(dtype);
    }

    public String storeFile(MultipartFile file) throws IOException {
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    public MDocuments saveDocument(MDocuments document) {
        return documentsRepository.save(document);
    }

}
