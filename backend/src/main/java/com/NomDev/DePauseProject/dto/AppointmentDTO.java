package com.NomDev.DePauseProject.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDTO {
    private Long id;
    private LocalDateTime appointmentTime;
    private String status;
    private String therapyType;
    private UserDTO user;
    private PsychologistDTO psychologist;
    private boolean hasReview;
}
