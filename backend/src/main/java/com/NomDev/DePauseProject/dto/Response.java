package com.NomDev.DePauseProject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int statusCode;
    private String message;
    private String token;
    private String role;
    private String expirationTime;

    private UserDTO user;
    private PsychologistDTO psychologist;
    private AppointmentDTO appointment;
    private ReviewDTO review;

    private List<UserDTO> userList;
    private List<PsychologistDTO> psychologistList;
    private List<AppointmentDTO> appointmentList;
    private List<ReviewDTO> reviewList;
    private String certificateUrl;

    @Setter
    private Page<?> page;

    private List<LocalDate> availableDates;

}

