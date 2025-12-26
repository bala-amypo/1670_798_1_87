package com.example.demo.service.impl;

import com.example.demo.entity.ActivityLog;
import com.example.demo.entity.ActivityType;
import com.example.demo.entity.EmissionFactor;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.ActivityLogRepository;
import com.example.demo.repository.ActivityTypeRepository;
import com.example.demo.repository.EmissionFactorRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ActivityLogService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {
    private final UserRepository userRepository;
    private final ActivityTypeRepository typeRepository;
    private final ActivityLogRepository logRepository;
    private final EmissionFactorRepository factorRepository;

    public ActivityLogServiceImpl(UserRepository userRepository, ActivityTypeRepository typeRepository, 
                                 ActivityLogRepository logRepository, EmissionFactorRepository factorRepository) {
        this.userRepository = userRepository;
        this.typeRepository = typeRepository;
        this.logRepository = logRepository;
        this.factorRepository = factorRepository;
    }

    @Override
    public List<ActivityLog> getLogsByUserAndDate(Long userId, LocalDate startDate, LocalDate endDate) {
        return logRepository.findByUser_IdAndActivityDateBetween(userId, startDate, endDate);
    }

    @Override
    public List<ActivityLog> getLogsByUser(Long userId) {
        return logRepository.findByUser_Id(userId);
    }

    @Override
    public ActivityLog logActivity(Long userId, Long activityTypeId, ActivityLog log) {
        if (log.getActivityDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Activity date cannot be in the future");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ActivityType type = typeRepository.findById(activityTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity type not found"));
        EmissionFactor factor = factorRepository.findByActivityType_Id(activityTypeId)
                .orElseThrow(() -> new ValidationException("No emission factor configured for this activity type"));

        log.setUser(user);
        log.setActivityType(type);
        log.setEstimatedEmission(log.getQuantity() * factor.getFactorValue());

        return logRepository.save(log);
    }
}