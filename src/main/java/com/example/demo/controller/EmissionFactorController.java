package com.example.demo.controller;

import com.example.demo.entity.EmissionFactor;
import com.example.demo.service.EmissionFactorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/factors")
public class EmissionFactorController {

    private final EmissionFactorService factorService;

    public EmissionFactorController(EmissionFactorService factorService) {
        this.factorService = factorService;
    }

    @GetMapping("/type/{typeId}")
    public EmissionFactor getByType(@PathVariable Long typeId) {
        return factorService.getFactorByType(typeId);
    }

    @GetMapping
    public List<EmissionFactor> getAll() {
        return factorService.getAllFactors();
    }
}
