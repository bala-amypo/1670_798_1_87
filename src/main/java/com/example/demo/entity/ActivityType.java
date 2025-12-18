package com.example.demo.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
public class ActivityType {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@Column(unique = true)
private String typeName;


private String unit;


@ManyToOne
@JoinColumn(name = "category_id")
private ActivityCategory category;


private LocalDateTime createdAt;


@PrePersist
protected void onCreate() {
this.createdAt = LocalDateTime.now();
}


public Long getId() { return id; }
public void setId(Long id) { this.id = id; }


public String getTypeName() { return typeName; }
public void setTypeName(String typeName) { this.typeName = typeName; }


public String getUnit() { return unit; }
public void setUnit(String unit) { this.unit = unit; }


public ActivityCategory getCategory() { return category; }
public void setCategory(ActivityCategory category) { this.category = category; }


public LocalDateTime getCreatedAt() { return createdAt; }
}