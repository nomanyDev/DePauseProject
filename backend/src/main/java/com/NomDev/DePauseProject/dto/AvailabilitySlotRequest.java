package com.NomDev.DePauseProject.dto;

import lombok.Data;

@Data
public class AvailabilitySlotRequest {
    private String date;
    private String startTime;
    private String endTime;
    private String status;
}
