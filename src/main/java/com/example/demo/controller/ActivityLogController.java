package com.example.demo.controller;

import com.example.demo.dto.ActivityLogRequest;
import com.example.demo.entity.ActivityLog;
import com.example.demo.service.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class ActivityLogController {
    
    @Autowired
    private ActivityLogService logService;
    
    @PostMapping("/{userId}/{typeId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == authentication.principal.userId)")
    public ResponseEntity<ActivityLog> logActivity(@PathVariable Long userId,
                                                  @PathVariable Long typeId,
                                                  @RequestBody ActivityLogRequest request) {
        ActivityLog log = new ActivityLog();
        log.setQuantity(request.getQuantity());
        log.setActivityDate(request.getActivityDate());
        
        ActivityLog created = logService.logActivity(userId, typeId, log);
        return ResponseEntity.ok(created);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ActivityLog> getLog(@PathVariable Long id) {
        ActivityLog log = logService.getLog(id);
        return ResponseEntity.ok(log);
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == authentication.principal.userId)")
    public ResponseEntity<List<ActivityLog>> getLogsByUser(@PathVariable Long userId) {
        List<ActivityLog> logs = logService.getLogsByUser(userId);
        return ResponseEntity.ok(logs);
    }
    
    @GetMapping("/user/{userId}/range")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == authentication.principal.userId)")
    public ResponseEntity<List<ActivityLog>> getLogsByUserAndDate(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        
        List<ActivityLog> logs = logService.getLogsByUserAndDate(userId, start, end);
        return ResponseEntity.ok(logs);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ActivityLog>> getAllLogs() {
        List<ActivityLog> logs = logService.getAllLogs();
        return ResponseEntity.ok(logs);
    }
}