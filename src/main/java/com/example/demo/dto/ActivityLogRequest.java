package com.example.demo.dto;

import java.time.LocalDate;

public class ActivityLogRequest {

    private Double quantity;
    private LocalDate activityDate;

    public ActivityLogRequest() {}

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public LocalDate getActivityDate() { return activityDate; }
    public void setActivityDate(LocalDate activityDate) { this.activityDate = activityDate; }
}
