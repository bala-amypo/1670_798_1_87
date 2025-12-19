package com.example.demo.controller;

import com.example.demo.entity.ActivityType;
import com.example.demo.service.ActivityTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/types")
@Tag(name = "Activity Types", description = "Activity type management endpoints")
public class ActivityTypeController {
    
    private final ActivityTypeService typeService;
    
    public ActivityTypeController(ActivityTypeService typeService) {
        this.typeService = typeService;
    }
    
    @PostMapping("/category/{categoryId}")
    @Operation(summary = "Create a new activity type under a category")
    public ActivityType createType(@PathVariable Long categoryId, @RequestBody ActivityType type) {
        return typeService.createType(categoryId, type);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get activity type by ID")
    public ActivityType getType(@PathVariable Long id) {
        return typeService.getType(id);
    }
    
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get all activity types by category ID")
    public List<ActivityType> getTypesByCategory(@PathVariable Long categoryId) {
        return typeService.getTypesByCategory(categoryId);
    }
}