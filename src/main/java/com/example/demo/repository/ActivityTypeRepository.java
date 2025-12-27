package com.example.demo.repository;

import com.example.demo.entity.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActivityTypeRepository extends JpaRepository<ActivityType, Long> {
    List<ActivityType> findByCategory_Id(Long categoryId);
}
