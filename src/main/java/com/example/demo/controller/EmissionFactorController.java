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

    @PostMapping("/{typeId}")
    public EmissionFactor create(@PathVariable Long typeId,
                                 @RequestBody EmissionFactor factor) {
        return service.createFactor(typeId, factor);
    }

    @GetMapping("/{id}")
    public EmissionFactor get(@PathVariable Long id) {
        return service.getFactor(id);
    }

    @GetMapping("/type/{typeId}")
    public EmissionFactor getByType(@PathVariable Long typeId) {
        return service.getFactorByType(typeId);
    }

    @GetMapping
    public List<EmissionFactor> getAll() {
        return service.getAllFactors();
    }
}
