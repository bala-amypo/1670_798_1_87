package com.example.demo.controller;

import com.example.demo.entity.ActivityCategory;
import com.example.demo.service.ActivityCategoryService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class ActivityCategoryController {
    private final ActivityCategoryService categoryService;

    public ActivityCategoryController(ActivityCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ActivityCategory createCategory(@RequestBody ActivityCategory category) {
        return categoryService.createCategory(category);
    }

    @GetMapping
    public List<ActivityCategory> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ActivityCategory getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }
}
