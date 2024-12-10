package com.NomDev.DePauseProject.controller;


import com.NomDev.DePauseProject.dto.Response;
import com.NomDev.DePauseProject.service.interfaces.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private IAppointmentService appointmentService;

    // Создание записи на прием
    @PostMapping("/book")
    public ResponseEntity<Response> createAppointment(
            @RequestParam Long psychologistId,
            @RequestParam String therapyType,
            @RequestParam String appointmentTime, // ISO-8601 формат (например: 2024-01-15T14:00:00)
            Authentication authentication
    ) {
        String email = authentication.getName();
        Response response = appointmentService.createAppointment(
                email, psychologistId, LocalDateTime.parse(appointmentTime), therapyType);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Получение истории записей пользователя
    @GetMapping("/history")
    public ResponseEntity<Response> getAppointmentsByUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        String email = authentication.getName();
        Pageable pageable = PageRequest.of(page, size);
        Response response = appointmentService.getAppointmentsByUser(email, pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Получение списка записей психолога
    @GetMapping("/client-list")
    @PreAuthorize("hasAuthority('PSYCHOLOGIST')")
    public ResponseEntity<Response> getAppointmentsByPsychologist(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        String email = authentication.getName();
        Pageable pageable = PageRequest.of(page, size);
        Response response = appointmentService.getAppointmentsByPsychologist(email, pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Отмена записи
    @DeleteMapping("/{appointmentId}/cancel")
    public ResponseEntity<Response> cancelAppointment(@PathVariable Long appointmentId) {
        Response response = appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
