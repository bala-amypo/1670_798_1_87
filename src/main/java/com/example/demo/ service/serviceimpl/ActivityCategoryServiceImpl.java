package com.example.demo.service.impl;

import com.example.demo.entity.ActivityCategory;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.ActivityCategoryRepository;
import com.example.demo.service.ActivityCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityCategoryServiceImpl implements ActivityCategoryService {
    
    private final ActivityCategoryRepository categoryRepository;
    
    @Autowired
    public ActivityCategoryServiceImpl(ActivityCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    @Override
    public ActivityCategory createCategory(ActivityCategory category) throws ValidationException {
        // Check duplicate category name
        if (categoryRepository.existsByCategoryName(category.getCategoryName())) {
            throw new ValidationException("Category name must be unique");
        }
        
        // Set creation timestamp
        category.setCreatedAt(LocalDateTime.now());
        
        return categoryRepository.save(category);
    }
    
    @Override
    public ActivityCategory getCategory(Long id) throws ResourceNotFoundException {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }
    
    @Override
    public List<ActivityCategory> getAllCategories() {
        return categoryRepository.findAll();
    }
}