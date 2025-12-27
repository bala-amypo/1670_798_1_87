package com.example.demo.security;

import com.example.demo.entity.ActivityLog;
import com.example.demo.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("activityLogSecurity")
public class ActivityLogSecurity {
    
    @Autowired
    private ActivityLogRepository activityLogRepository;
    
    public boolean isOwner(Authentication authentication, Long logId) {
        ActivityLog log = activityLogRepository.findById(logId).orElse(null);
        if (log == null) {
            return false;
        }
        
        Long userId = JwtUtil.extractUserIdFromAuthentication(authentication);
        return log.getUser().getId().equals(userId);
    }
}