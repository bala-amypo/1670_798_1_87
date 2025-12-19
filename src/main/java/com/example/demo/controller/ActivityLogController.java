package com.example.demo.controller;

import com.example.demo.dto.ActivityLogRequest;
import com.example.demo.entity.ActivityLog;
import com.example.demo.service.ActivityLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
@Tag(name = "Activity Logs", description = "Activity logging and retrieval endpoints")
public class ActivityLogController {
    
    private final ActivityLogService logService;
    
    public ActivityLogController(ActivityLogService logService) {
        this.logService = logService;
    }
    
    @PostMapping("/{userId}/{typeId}")
    @Operation(summary = "Log a new activity")
    public ActivityLog logActivity(@PathVariable Long userId, 
                                  @PathVariable Long typeId,
                                  @RequestBody ActivityLogRequest request) {
        ActivityLog log = new ActivityLog();
        log.setQuantity(request.getQuantity());
        log.setActivityDate(request.getActivityDate());
        
        return logService.logActivity(userId, typeId, log);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all activity logs for a user")
    public List<ActivityLog> getLogsByUser(@PathVariable Long userId) {
        return logService.getLogsByUser(userId);
    }
    
    @GetMapping("/user/{userId}/range")
    @Operation(summary = "Get activity logs for a user within a date range")
    public List<ActivityLog> getLogsByUserAndDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return logService.getLogsByUserAndDate(userId, start, end);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get activity log by ID")
    public ActivityLog getLog(@PathVariable Long id) {
        return logService.getLog(id);
    }
}