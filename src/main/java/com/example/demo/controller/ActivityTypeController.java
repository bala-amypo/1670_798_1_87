package com.example.demo.controller;

import com.example.demo.entity.ActivityType;
import com.example.demo.repository.ActivityTypeRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/types")
public class ActivityTypeController {
    private final ActivityTypeRepository typeRepository;

    public ActivityTypeController(ActivityTypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @GetMapping("/category/{categoryId}")
    public List<ActivityType> getTypesByCategory(@PathVariable Long categoryId) {
        return typeRepository.findByCategory_Id(categoryId);
    }
}
