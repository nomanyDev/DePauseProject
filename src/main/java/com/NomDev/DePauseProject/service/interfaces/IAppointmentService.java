package com.NomDev.DePauseProject.service.interfaces;

import com.NomDev.DePauseProject.dto.Response;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface IAppointmentService {
    Response createAppointment(Long userId, Long psychologistId, LocalDateTime appointmentTime, String therapyType); // Создание записи
    Response getAppointmentsByUser(Long userId, Pageable pageable); // Получение записей пользователя
    Response getAppointmentsByPsychologist(Long psychologistId, Pageable pageable); // Получение записей психолога
    Response cancelAppointment(Long appointmentId); // Отмена записи
}
