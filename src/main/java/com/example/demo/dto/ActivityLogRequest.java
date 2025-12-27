package com.example.demo.dto;

import java.time.LocalDate;

public class ActivityLogRequest {

    private Double quantity;
    private LocalDate activityDate;

    public ActivityLogRequest() {
    }

    public ActivityLogRequest(Double quantity, LocalDate activityDate) {
        this.quantity = quantity;
        this.activityDate = activityDate;
    }

    public Double getQuantity() {
        return quantity;
    }

    public LocalDate getActivityDate() {
        return activityDate;
    }
}
