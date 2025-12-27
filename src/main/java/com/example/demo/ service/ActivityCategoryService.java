package com.example.demo.service;

import com.example.demo.entity.ActivityCategory;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ValidationException;

import java.util.List;

public interface ActivityCategoryService {
    ActivityCategory createCategory(ActivityCategory category) throws ValidationException;
    ActivityCategory getCategory(Long id) throws ResourceNotFoundException;
    List<ActivityCategory> getAllCategories();
}