package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.*;
import com.example.demo.service.ActivityLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogRepository logRepository;
    private final UserRepository userRepository;
    private final ActivityTypeRepository typeRepository;
    private final EmissionFactorRepository factorRepository;

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
    public ActivityLog logActivity(Long userId, Long typeId, ActivityLog log) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        ActivityType type = typeRepository.findById(typeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        if (log.getActivityDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Activity date cannot be in the future");
        }

        EmissionFactor factor = factorRepository
                .findByActivityType_Id(typeId)
                .orElseThrow(() ->
                        new ValidationException("No emission factor configured"));

        double emission = log.getQuantity() * factor.getFactorValue();

        log.setUser(user);
        log.setActivityType(type);
        log.setEstimatedEmission(emission);

        return logRepository.save(log);
    }

    @Override
    public List<ActivityLog> getLogsByUser(Long userId) {
        return logRepository.findByUser_Id(userId);
    }

    @Override
    public List<ActivityLog> getLogsByUserAndDate(
            Long userId, LocalDate start, LocalDate end) {

        return logRepository.findByUser_IdAndActivityDateBetween(
                userId, start, end);
    }
}
