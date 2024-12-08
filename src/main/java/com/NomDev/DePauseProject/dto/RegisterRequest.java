package com.NomDev.DePauseProject.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String gender; // "M", "F", "U"
    private Integer age; // Необязательное поле
    private String phoneNumber;
    private String telegramNickname;
    private String role; // USER или PSYCHOLOGIST
}
