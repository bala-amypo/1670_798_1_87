package com.example.demo.controller;

import com.example.demo.entity.ActivityType;
import com.example.demo.service.ActivityTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/types")
public class ActivityTypeController {

    private final ActivityTypeService service;

    public ActivityTypeController(ActivityTypeService service) {
        this.service = service;
    }

    @PostMapping("/category/{categoryId}")
    public ActivityType create(@PathVariable Long categoryId,
                               @RequestBody ActivityType type) {
        return service.createType(categoryId, type);
    }

    @GetMapping("/{id}")
    public ActivityType get(@PathVariable Long id) {
        return service.getType(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<ActivityType> getByCategory(@PathVariable Long categoryId) {
        return service.getTypesByCategory(categoryId);
    }
}
