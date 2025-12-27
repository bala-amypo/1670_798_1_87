package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ActivityLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_type_id", nullable = false)
    private ActivityType activityType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private Double quantity;
    
    @Column(nullable = false)
    private LocalDate activityDate;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime loggedAt;
    
    @Column(nullable = false)
    private Double estimatedEmission;
    
    @PrePersist
    public void prePersist() {
        if (loggedAt == null) {
            loggedAt = LocalDateTime.now();
        }
    }
}