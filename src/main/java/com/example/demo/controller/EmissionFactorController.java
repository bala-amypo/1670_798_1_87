package com.example.demo.controller;

import com.example.demo.entity.EmissionFactor;
import com.example.demo.service.EmissionFactorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/factors")
public class EmissionFactorController {

    private final EmissionFactorService service;

    public EmissionFactorController(EmissionFactorService service) {
        this.service = service;
    }

    @PostMapping("/{activityTypeId}")
    public EmissionFactor create(@PathVariable Long activityTypeId,
                                 @RequestBody EmissionFactor factor) {
        return service.createFactor(activityTypeId, factor);
    }

    @GetMapping("/{id}")
    public EmissionFactor getById(@PathVariable Long id) {
        return service.getFactor(id);
    }

    @GetMapping("/type/{activityTypeId}")
    public EmissionFactor getByType(@PathVariable Long activityTypeId) {
        return service.getFactorByType(activityTypeId);
    }

    @GetMapping
    public List<EmissionFactor> getAll() {
        return service.getAllFactors();
    }
}
