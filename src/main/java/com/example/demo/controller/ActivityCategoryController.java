package com.example.demo.controller;

import com.example.demo.entity.ActivityCategory;
import com.example.demo.service.ActivityCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Activity Categories", description = "Activity category management endpoints")
public class ActivityCategoryController {
    
    private final ActivityCategoryService categoryService;
    
    public ActivityCategoryController(ActivityCategoryService categoryService) {
        this.categoryService = categoryService;
    }
    
    @PostMapping
    @Operation(summary = "Create a new activity category")
    public ActivityCategory createCategory(@RequestBody ActivityCategory category) {
        return categoryService.createCategory(category);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID")
    public ActivityCategory getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }
    
    @GetMapping
    @Operation(summary = "Get all categories")
    public List<ActivityCategory> getAllCategories() {
        return categoryService.getAllCategories();
    }
}