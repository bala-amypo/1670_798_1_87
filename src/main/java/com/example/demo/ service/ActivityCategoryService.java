package com.example.demo.service;

import com.example.demo.entity.ActivityCategory;
import java.util.List;

public interface ActivityCategoryService {
    ActivityCategory createCategory(ActivityCategory category);
    List<ActivityCategory> getAllCategories();
    ActivityCategory getCategory(Long id);
}
