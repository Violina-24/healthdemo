package com.health.healthdemo.repository;

import com.health.healthdemo.entity.MDocuments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MDocumentsRepository extends JpaRepository<MDocuments,Long> {
    List<MDocuments> findByDtype(String dtype);
}
