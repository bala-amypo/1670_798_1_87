package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.*;
import com.example.demo.service.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {
    
    private final ActivityLogRepository logRepository;
    private final UserRepository userRepository;
    private final ActivityTypeRepository typeRepository;
    private final EmissionFactorRepository factorRepository;
    
    @Autowired
    public ActivityLogServiceImpl(ActivityLogRepository logRepository,
                                 UserRepository userRepository,
                                 ActivityTypeRepository typeRepository,
                                 EmissionFactorRepository factorRepository) {
        this.logRepository = logRepository;
        this.userRepository = userRepository;
        this.typeRepository = typeRepository;
        this.factorRepository = factorRepository;
    }
    
    @Override
    public ActivityLog logActivity(Long userId, Long typeId, ActivityLog log) 
            throws ResourceNotFoundException, ValidationException {
        
        // Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Find activity type
        ActivityType activityType = typeRepository.findById(typeId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity type not found"));
        
        // Validate quantity
        if (log.getQuantity() == null || log.getQuantity() <= 0) {
            throw new ValidationException("Quantity must be greater than zero");
        }
        
        // Validate activity date
        if (log.getActivityDate() == null) {
            throw new ValidationException("Activity date is required");
        }
        
        if (log.getActivityDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Activity date cannot be in the future");
        }
        
        // Find emission factor
        EmissionFactor factor = factorRepository.findByActivityType_Id(typeId)
                .orElseThrow(() -> new ValidationException("No emission factor configured"));
        
        // Calculate estimated emission
        Double estimatedEmission = log.getQuantity() * factor.getFactorValue();
        
        // Set properties
        log.setUser(user);
        log.setActivityType(activityType);
        log.setEstimatedEmission(estimatedEmission);
        log.setLoggedAt(LocalDateTime.now());
        
        return logRepository.save(log);
    }
    
    @Override
    public ActivityLog getLog(Long id) throws ResourceNotFoundException {
        return logRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity log not found"));
    }
    
    @Override
    public List<ActivityLog> getAllLogs() {
        return logRepository.findAll();
    }
    
    @Override
    public List<ActivityLog> getLogsByUser(Long userId) {
        return logRepository.findByUser_Id(userId);
    }
    
    @Override
    public List<ActivityLog> getLogsByUserAndDate(Long userId, LocalDate start, LocalDate end) {
        return logRepository.findByUser_IdAndActivityDateBetween(userId, start, end);
    }
}