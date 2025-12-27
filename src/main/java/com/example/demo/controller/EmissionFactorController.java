package com.example.demo.controller;

import com.example.demo.entity.EmissionFactor;
import com.example.demo.service.EmissionFactorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/factors")
@Tag(name = "Emission Factors", description = "Emission Factor APIs")
public class EmissionFactorController {
    private final EmissionFactorService factorService;

    public EmissionFactorController(EmissionFactorService factorService) {
        this.factorService = factorService;
    }

    @GetMapping("/type/{typeId}")
    @Operation(summary = "Get emission factor by activity type ID")
    public EmissionFactor getFactorByType(@PathVariable Long typeId) {
        return factorService.getFactorByType(typeId);
    }
}