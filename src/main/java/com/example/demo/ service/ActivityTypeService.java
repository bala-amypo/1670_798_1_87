package com.example.demo.service;

import com.example.demo.entity.ActivityType;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;

import java.util.List;

public interface ActivityTypeService {
    ActivityType createType(Long categoryId, ActivityType type) throws ResourceNotFoundException, ValidationException;
    ActivityType getType(Long id) throws ResourceNotFoundException;
    List<ActivityType> getAllTypes();
    List<ActivityType> getTypesByCategory(Long categoryId);
}