package com.example.demo.repository;

import com.example.demo.entity.EmissionFactor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmissionFactorRepository extends JpaRepository<EmissionFactor, Long> {

    Optional<EmissionFactor> findByActivityType_Id(Long typeId);
}
