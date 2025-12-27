package com.example.demo.controller;

import com.example.demo.entity.ActivityType;
import com.example.demo.service.ActivityTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/types")
public class ActivityTypeController {
    
    @Autowired
    private ActivityTypeService typeService;
    
    @PostMapping("/category/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ActivityType> createType(@PathVariable Long categoryId, 
                                                   @Valid @RequestBody ActivityType type) {
        ActivityType created = typeService.createType(categoryId, type);
        return ResponseEntity.ok(created);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ActivityType> getType(@PathVariable Long id) {
        ActivityType type = typeService.getType(id);
        return ResponseEntity.ok(type);
    }
    
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ActivityType>> getTypesByCategory(@PathVariable Long categoryId) {
        List<ActivityType> types = typeService.getTypesByCategory(categoryId);
        return ResponseEntity.ok(types);
    }
    
    @GetMapping
    public ResponseEntity<List<ActivityType>> getAllTypes() {
        List<ActivityType> types = typeService.getAllTypes();
        return ResponseEntity.ok(types);
    }
}