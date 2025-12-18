package com.example.demo.repository;


import com.example.demo.entity.EmissionFactor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmissionFactorRepository extends JpaRepository<EmissionFactor, Long> {
EmissionFactor findByActivityType_Id(Long typeId);
}