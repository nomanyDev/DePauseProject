package com.NomDev.DePauseProject.service.impl;

import com.NomDev.DePauseProject.dto.AppointmentDTO;
import com.NomDev.DePauseProject.dto.Response;
import com.NomDev.DePauseProject.entity.Appointment;
import com.NomDev.DePauseProject.entity.Availability;
import com.NomDev.DePauseProject.entity.PsychologistDetails;
import com.NomDev.DePauseProject.entity.User;
import com.NomDev.DePauseProject.exception.OurException;
import com.NomDev.DePauseProject.repository.AppointmentRepository;
import com.NomDev.DePauseProject.repository.AvailabilityRepository;
import com.NomDev.DePauseProject.repository.PsychologistDetailsRepository;
import com.NomDev.DePauseProject.repository.UserRepository;
import com.NomDev.DePauseProject.service.NotificationService;
import com.NomDev.DePauseProject.service.interfaces.IAppointmentService;
import com.NomDev.DePauseProject.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService implements IAppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PsychologistDetailsRepository psychologistDetailsRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    // Метод для проверки доступности даты
    private boolean isAvailableDate(LocalDateTime requestedTime, List<Appointment> existingAppointments) {
        return existingAppointments.stream()
                .noneMatch(existingAppointment ->
                        !requestedTime.isBefore(existingAppointment.getAppointmentTime()) &&
                                !requestedTime.isAfter(existingAppointment.getAppointmentTime().plusHours(1))
                );
    }

    @Override
    public boolean isAvailableDate(Long psychologistId, LocalDateTime appointmentTime) {
        List<Appointment> existingAppointments = appointmentRepository.findByPsychologistId(psychologistId);
        return isAvailableDate(appointmentTime, existingAppointments);
    }

    @Override
    public Response createAppointment(Long userId, Long psychologistId, LocalDateTime appointmentTime, String therapyType) {
        Response response = new Response();
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User not found"));
            PsychologistDetails psychologistDetails = psychologistDetailsRepository.findById(psychologistId)
                    .orElseThrow(() -> new OurException("Psychologist not found"));

            List<Appointment> existingAppointments = appointmentRepository.findByPsychologistId(psychologistId);


            if (!isAvailableDate(appointmentTime, existingAppointments)) {
                response.setStatusCode(409); // Conflict
                response.setMessage("The appointment time is already booked.");
                return response;
            }

            Appointment appointment = new Appointment();
            appointment.setUser(user);
            appointment.setPsychologist(psychologistDetails);
            appointment.setAppointmentTime(appointmentTime);
            appointment.setTherapyType(therapyType);
            appointment.setStatus("Scheduled");

            Appointment savedAppointment = appointmentRepository.save(appointment);

            AppointmentDTO appointmentDTO = Utils.mapAppointmentToDTO(savedAppointment);
            response.setStatusCode(201);
            response.setMessage("Appointment created successfully");
            response.setAppointment(appointmentDTO);

            sendNotificationsAsync(user, psychologistDetails, appointmentTime);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error creating appointment: " + e.getMessage());
        }
        return response;
    }

    @Async
    public void sendNotificationsAsync(User user, PsychologistDetails psychologist, LocalDateTime appointmentTime) {
        try {
            String userEmail = user.getEmail();
            String psychologistEmail = psychologist.getUser().getEmail();

            String subject = "Appointment Scheduled";
            String userBody = "Dear " + user.getFirstName() + ",\n\nYour appointment with " +
                    psychologist.getUser().getFirstName() + " is scheduled for " + appointmentTime + ".\n\nThank you!";
            String psychologistBody = "Dear " + psychologist.getUser().getFirstName() + ",\n\nYou have a new appointment with " +
                    user.getFirstName() + " on " + appointmentTime + ".\n\nThank you!";

            notificationService.sendEmail(userEmail, subject, userBody);
            notificationService.sendEmail(psychologistEmail, subject, psychologistBody);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Response getAppointmentsByUser(Long userId, Pageable pageable) {
        Response response = new Response();
        try {
            Page<Appointment> appointments = appointmentRepository.findByUserId(userId, pageable);
            Page<AppointmentDTO> appointmentDTOs = appointments.map(Utils::mapAppointmentToDTO);

            response.setStatusCode(200);
            response.setMessage("Appointments fetched successfully");
            response.setPage(appointmentDTOs);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching appointments: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAppointmentsByPsychologist(Long psychologistId, Pageable pageable) {
        Response response = new Response();
        try {
            Page<Appointment> appointments = appointmentRepository.findByPsychologistId(psychologistId, pageable);
            Page<AppointmentDTO> appointmentDTOs = appointments.map(Utils::mapAppointmentToDTO);

            response.setStatusCode(200);
            response.setMessage("Appointments fetched successfully");
            response.setPage(appointmentDTOs);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching appointments: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelAppointment(Long appointmentId) {
        Response response = new Response();
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new OurException("Appointment not found"));

            appointment.setStatus("Cancelled");
            appointmentRepository.save(appointment);

            AppointmentDTO appointmentDTO = Utils.mapAppointmentToDTO(appointment);
            response.setStatusCode(200);
            response.setMessage("Appointment cancelled successfully");
            response.setAppointment(appointmentDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error cancelling appointment: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableDates(Long psychologistId, LocalDate fromDate, LocalDate toDate) {
        Response response = new Response();
        try {
            List<Availability> availableDates = availabilityRepository
                    .findByPsychologistIdAndAvailableDateBetween(psychologistId, fromDate, toDate);
            response.setStatusCode(200);
            response.setMessage("Available dates fetched successfully");
            response.setAvailableDates(availableDates.stream().map(Availability::getAvailableDate).toList());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching available dates: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateAvailability(Long psychologistId, List<String> availableDates) {
        Response response = new Response();
        try {
            List<Availability> availabilities = availableDates.stream()
                    .map(date -> new Availability(
                            psychologistDetailsRepository.findById(psychologistId)
                                    .orElseThrow(() -> new OurException("Psychologist not found")),
                            LocalDate.parse(date)))
                    .toList();

            availabilityRepository.saveAll(availabilities);

            response.setStatusCode(200);
            response.setMessage("Availability updated successfully");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating availability: " + e.getMessage());
        }
        return response;
    }
}

