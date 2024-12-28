package com.NomDev.DePauseProject.controller;


import com.NomDev.DePauseProject.dto.AvailabilitySlotRequest;
import com.NomDev.DePauseProject.dto.Response;
import com.NomDev.DePauseProject.service.interfaces.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private IAppointmentService appointmentService;

    @PostMapping("/book")
    public ResponseEntity<Response> createAppointment(
            @RequestParam Long userId,
            @RequestParam Long psychologistId,
            @RequestParam String therapyType,
            @RequestParam String appointmentTime
    ) {
        Response response = appointmentService.createAppointment(
                userId, psychologistId, LocalDateTime.parse(appointmentTime), therapyType);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/history")
    public ResponseEntity<Response> getAppointmentsByUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam Long userId // Передаем userId напрямую
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Response response = appointmentService.getAppointmentsByUser(userId, pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/client-list")
    @PreAuthorize("hasAuthority('PSYCHOLOGIST')")
    public ResponseEntity<Response> getAppointmentsByPsychologist(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam Long psychologistId
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Response response = appointmentService.getAppointmentsByPsychologist(psychologistId, pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{appointmentId}/cancel")
    public ResponseEntity<Response> cancelAppointment(@PathVariable Long appointmentId) {
        Response response = appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/available-dates")
    public ResponseEntity<Response> getAvailableDates(
            @RequestParam Long psychologistId,
            @RequestParam String fromDate,
            @RequestParam String toDate
    ) {
        Response response = appointmentService.getAvailableDates(
                psychologistId, LocalDate.parse(fromDate), LocalDate.parse(toDate));
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update-availability")
    @PreAuthorize("hasAuthority('PSYCHOLOGIST')")
    public ResponseEntity<Response> updateAvailability(
            @RequestParam Long psychologistId,
            @RequestBody List<String> availableDates
    ) {
        Response response = appointmentService.updateAvailability(psychologistId, availableDates);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update-slots")
    @PreAuthorize("hasAuthority('PSYCHOLOGIST')")
    public ResponseEntity<Response> updateAvailabilitySlots(
            @RequestParam Long psychologistId,
            @RequestBody List<AvailabilitySlotRequest> slots
    ) {
        Response response = appointmentService.updateAvailabilitySlots(psychologistId, slots);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/available-time-slots")
    public ResponseEntity<Response> getAvailableTimeSlots(
            @RequestParam Long psychologistId,
            @RequestParam String date
    ) {
        Response response = appointmentService.getAvailableTimeSlots(psychologistId, LocalDate.parse(date));
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
