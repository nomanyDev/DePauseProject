package com.NomDev.DePauseProject.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {
    private Long id;
    private String firstName; // Имя пользователя
    private String lastName; // Фамилия пользователя
    private String email;
    private String profilePhotoUrl;
    private LocalDate birthDate; // Дата рождения
    private Integer age; // Расчетный возраст
    private String gender;
    private String phoneNumber;
    private String telegramNickname;
    private String role;
}
