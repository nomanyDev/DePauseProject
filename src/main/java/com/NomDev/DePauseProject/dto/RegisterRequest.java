package com.NomDev.DePauseProject.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String firstName; // Имя
    private String lastName; // Фамилия
    private String email;
    private String password;
    private String gender; // "M", "F", "U"
    private LocalDate birthDate; // Дата рождения
    private String phoneNumber;
    private String telegramNickname;
    private String role; // USER или PSYCHOLOGIST
}
