package com.example.demo.controller;

import com.example.demo.entity.ActivityType;
import com.example.demo.repository.ActivityTypeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/types")
@Tag(name = "Activity Types", description = "Activity Type APIs")
public class ActivityTypeController {
    private final ActivityTypeRepository typeRepository;

    public ActivityTypeController(ActivityTypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get activity types by category ID")
    public List<ActivityType> getTypesByCategory(@PathVariable Long categoryId) {
        return typeRepository.findByCategory_Id(categoryId);
    }
}