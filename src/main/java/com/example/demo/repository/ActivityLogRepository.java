package com.example.demo.repository;

import com.example.demo.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByUser_IdAndActivityDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    List<ActivityLog> findByUser_Id(Long userId);
}