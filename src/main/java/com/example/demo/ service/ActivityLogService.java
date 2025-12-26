package com.example.demo.service;

import com.example.demo.entity.ActivityLog;
import java.time.LocalDate;
import java.util.List;

public interface ActivityLogService {
    List<ActivityLog> getLogsByUserAndDate(Long userId, LocalDate startDate, LocalDate endDate);
    List<ActivityLog> getLogsByUser(Long userId);
    ActivityLog logActivity(Long userId, Long activityTypeId, ActivityLog log);
}
