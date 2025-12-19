package com.example.demo.service;

import com.example.demo.entity.ActivityLog;
import java.time.LocalDate;
import java.util.List;

public interface ActivityLogService {
    ActivityLog logActivity(Long userId, Long typeId, ActivityLog log);
    ActivityLog getLog(Long id);
    List<ActivityLog> getLogsByUser(Long userId);
    List<ActivityLog> getLogsByUserAndDate(Long userId, LocalDate start, LocalDate end);
}
