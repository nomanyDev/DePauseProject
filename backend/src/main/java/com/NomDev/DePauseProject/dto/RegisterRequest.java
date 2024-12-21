package com.NomDev.DePauseProject.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String gender;
    private LocalDate birthDate;
    private String phoneNumber;
    private String telegramNickname;
    private String role;
}
