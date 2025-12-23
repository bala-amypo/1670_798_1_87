package com.example.demo.controller;

import com.example.demo.entity.ActivityType;
import com.example.demo.service.ActivityTypeService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/types")
@Tag(name = "Activity Types")
public class ActivityTypeController {

    private final ActivityTypeService typeService;

    public ActivityTypeController(ActivityTypeService typeService) {
        this.typeService = typeService;
    }

    @PostMapping("/category/{categoryId}")
    public ActivityType createType(@PathVariable Long categoryId,
                                   @RequestBody ActivityType type) {
        return typeService.createType(categoryId, type);
    }

    @GetMapping("/category/{categoryId}")
    public List<ActivityType> getByCategory(@PathVariable Long categoryId) {
        return typeService.getTypesByCategory(categoryId);
    }

    @GetMapping("/{id}")
    public ActivityType getById(@PathVariable Long id) {
        return typeService.getType(id);
    }
}
