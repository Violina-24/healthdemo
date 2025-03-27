package com.health.healthdemo.repository;

import com.health.healthdemo.entity.MCouncellingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MCouncellingScheduleRepository extends JpaRepository<MCouncellingSchedule, Long> {
    Optional<MCouncellingSchedule> findTopByOrderByIdDesc();
}
