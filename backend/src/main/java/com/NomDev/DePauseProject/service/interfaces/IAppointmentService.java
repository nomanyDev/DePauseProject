package com.NomDev.DePauseProject.service.interfaces;

import com.NomDev.DePauseProject.dto.AvailabilitySlotRequest;
import com.NomDev.DePauseProject.dto.Response;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentService {
    Response createAppointment(Long userId, Long psychologistId, LocalDateTime appointmentTime, String therapyType); // Создание записи
    Response getAppointmentsByUser(Long userId, Pageable pageable); // Получение записей пользователя
    Response getAppointmentsByPsychologist(Long psychologistId, Pageable pageable); // Получение записей психолога
    Response cancelAppointment(Long appointmentId);
    Response getAvailableDates(Long psychologistId, LocalDate fromDate, LocalDate toDate);
    Response updateAvailability(Long psychologistId, List<String> availableDates);
    Response getAvailableTimeSlots(Long psychologistId, LocalDate date);
    Response updateAvailabilitySlots(Long psychologistId, List<AvailabilitySlotRequest> slots);
    Response isAvailableDate(Long psychologistId, LocalDateTime appointmentTime);
}
