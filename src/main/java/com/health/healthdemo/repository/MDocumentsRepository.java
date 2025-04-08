package com.health.healthdemo.repository;

import com.health.healthdemo.entity.MDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MDocumentsRepository extends JpaRepository<MDocuments,Long> {
    List<MDocuments> findByDtype(String dtype);

}
