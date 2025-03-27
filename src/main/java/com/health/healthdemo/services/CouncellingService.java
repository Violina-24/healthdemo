package com.health.healthdemo.services;

import com.health.healthdemo.repository.MCouncellingScheduleRepository;
import com.health.healthdemo.entity.MCouncellingSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CouncellingService {
    @Autowired
    private MCouncellingScheduleRepository scheduleRepository;

    // Check if counselling is currently active
    public boolean isCouncellingActive() {
        return scheduleRepository.findTopByOrderByIdDesc()
                .map(schedule -> {
                    LocalDateTime now = LocalDateTime.now();
                    return now.isAfter(schedule.getStartDate()) && now.isBefore(schedule.getEndDate());
                })
                .orElse(false);
    }

    // Retrieve the counselling deadline
    public LocalDate getCounsellingDeadline() {
        Optional<MCouncellingSchedule> latestSchedule = scheduleRepository.findTopByOrderByIdDesc();
        return latestSchedule.map(schedule -> schedule.getEndDate().toLocalDate()).orElse(LocalDate.MIN);
    }
}
