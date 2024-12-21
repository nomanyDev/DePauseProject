package com.NomDev.DePauseProject.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EditUserRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private LocalDate birthDate;
    private String role;
}
