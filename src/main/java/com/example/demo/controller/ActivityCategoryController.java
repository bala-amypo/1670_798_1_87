package com.example.demo.controller;

import com.example.demo.entity.ActivityCategory;
import com.example.demo.service.ActivityCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class ActivityCategoryController {
    
    @Autowired
    private ActivityCategoryService categoryService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ActivityCategory> createCategory(@RequestBody ActivityCategory category) {
        ActivityCategory created = categoryService.createCategory(category);
        return ResponseEntity.ok(created);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ActivityCategory> getCategory(@PathVariable Long id) {
        ActivityCategory category = categoryService.getCategory(id);
        return ResponseEntity.ok(category);
    }
    
    @GetMapping
    public ResponseEntity<List<ActivityCategory>> getAllCategories() {
        List<ActivityCategory> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}