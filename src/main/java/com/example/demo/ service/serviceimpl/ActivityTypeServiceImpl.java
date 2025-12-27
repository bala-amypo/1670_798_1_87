package com.example.demo.service.impl;

import com.example.demo.entity.ActivityCategory;
import com.example.demo.entity.ActivityType;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.ActivityCategoryRepository;
import com.example.demo.repository.ActivityTypeRepository;
import com.example.demo.service.ActivityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityTypeServiceImpl implements ActivityTypeService {
    
    private final ActivityTypeRepository typeRepository;
    private final ActivityCategoryRepository categoryRepository;
    
    @Autowired
    public ActivityTypeServiceImpl(ActivityTypeRepository typeRepository, 
                                  ActivityCategoryRepository categoryRepository) {
        this.typeRepository = typeRepository;
        this.categoryRepository = categoryRepository;
    }
    
    @Override
    public ActivityType createType(Long categoryId, ActivityType type) 
            throws ResourceNotFoundException, ValidationException {
        
        // Validate required fields
        if (type.getTypeName() == null || type.getTypeName().trim().isEmpty()) {
            throw new ValidationException("Type name is required");
        }
        
        if (type.getUnit() == null || type.getUnit().trim().isEmpty()) {
            throw new ValidationException("Unit is required");
        }
        
        // Find category
        ActivityCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        
        // Set category and timestamp
        type.setCategory(category);
        type.setCreatedAt(LocalDateTime.now());
        
        return typeRepository.save(type);
    }
    
    @Override
    public ActivityType getType(Long id) throws ResourceNotFoundException {
        return typeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity type not found"));
    }
    
    @Override
    public List<ActivityType> getAllTypes() {
        return typeRepository.findAll();
    }
    
    @Override
    public List<ActivityType> getTypesByCategory(Long categoryId) {
        return typeRepository.findByCategory_Id(categoryId);
    }
}