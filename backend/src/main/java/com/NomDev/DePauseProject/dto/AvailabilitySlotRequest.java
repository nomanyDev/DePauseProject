package com.NomDev.DePauseProject.dto;

import lombok.Data;

@Data
public class AvailabilitySlotRequest {
    private String date; // "yyyy-MM-dd"
    private String startTime; // "HH:mm"
    private String endTime; // "HH:mm"
    private String status; // "AVAILABLE" or "UNAVAILABLE"
}
