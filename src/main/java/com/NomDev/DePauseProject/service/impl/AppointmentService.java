package com.NomDev.DePauseProject.service.impl;

import com.NomDev.DePauseProject.dto.AppointmentDTO;
import com.NomDev.DePauseProject.dto.Response;
import com.NomDev.DePauseProject.entity.Appointment;
import com.NomDev.DePauseProject.entity.PsychologistDetails;
import com.NomDev.DePauseProject.entity.User;
import com.NomDev.DePauseProject.exception.OurException;
import com.NomDev.DePauseProject.repository.AppointmentRepository;
import com.NomDev.DePauseProject.repository.PsychologistDetailsRepository;
import com.NomDev.DePauseProject.repository.UserRepository;
import com.NomDev.DePauseProject.service.interfaces.IAppointmentService;
import com.NomDev.DePauseProject.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AppointmentService implements IAppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PsychologistDetailsRepository psychologistDetailsRepository;

    @Override
    public Response createAppointment(Long userId, Long psychologistId, LocalDateTime appointmentTime, String therapyType) {
        Response response = new Response();
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User not found"));
            PsychologistDetails psychologistDetails = psychologistDetailsRepository.findById(psychologistId)
                    .orElseThrow(() -> new OurException("Psychologist not found"));

            Appointment appointment = new Appointment();
            appointment.setUser(user);
            appointment.setPsychologist(psychologistDetails.getUser());
            appointment.setAppointmentTime(appointmentTime);
            appointment.setTherapyType(therapyType);
            appointment.setStatus("Scheduled");

            Appointment savedAppointment = appointmentRepository.save(appointment);

            AppointmentDTO appointmentDTO = Utils.mapAppointmentToDTO(savedAppointment);
            response.setStatusCode(200);
            response.setMessage("Appointment created successfully");
            response.setAppointment(appointmentDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error creating appointment: " + e.getMessage());
        }
        return response;
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
}

