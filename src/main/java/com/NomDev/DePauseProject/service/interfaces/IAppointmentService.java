package com.NomDev.DePauseProject.service.interfaces;

import com.NomDev.DePauseProject.dto.Response;

import java.time.LocalDateTime;

public interface IAppointmentService {
    Response createAppointment(Long userId, Long psychologistId, LocalDateTime appointmentTime, String therapyType);
    Response getAppointmentsByUser(Long userId);
    Response getAppointmentsByPsychologist(Long psychologistId);
    Response cancelAppointment(Long appointmentId);
}
