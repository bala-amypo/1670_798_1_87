package com.example.demo.service;

import com.example.demo.entity.ActivityLog;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;

import java.time.LocalDate;
import java.util.List;

public interface ActivityLogService {
    ActivityLog logActivity(Long userId, Long typeId, ActivityLog log) 
            throws ResourceNotFoundException, ValidationException;
    ActivityLog getLog(Long id) throws ResourceNotFoundException;
    List<ActivityLog> getAllLogs();
    List<ActivityLog> getLogsByUser(Long userId);
    List<ActivityLog> getLogsByUserAndDate(Long userId, LocalDate start, LocalDate end);
}