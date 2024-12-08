package com.NomDev.DePauseProject.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDTO {
    private Long id;
    private LocalDateTime appointmentTime; // Дата и время приема
    private String status; // Статус записи (например, CONFIRMED, CANCELLED)
    private String therapyType; // Тип терапии
    private UserDTO user; // Информация о клиенте
    private PsychologistDTO psychologist; // Информация о психологе
}
