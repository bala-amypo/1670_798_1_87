package com.example.demo.controller;

import com.example.demo.entity.ActivityCategory;
import com.example.demo.service.ActivityCategoryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Activity Categories")
@SecurityRequirement(name = "bearerAuth")   // 🔥 THIS IS THE FIX
public class ActivityCategoryController {

    private final ActivityCategoryService categoryService;

    public ActivityCategoryController(ActivityCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ActivityCategory create(@RequestBody ActivityCategory category) {
        return categoryService.createCategory(category);
    }

    @GetMapping
    public List<ActivityCategory> getAll() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ActivityCategory get(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }
}
