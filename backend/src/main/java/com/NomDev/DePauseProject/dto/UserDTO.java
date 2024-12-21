package com.NomDev.DePauseProject.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String profilePhotoUrl;
    private LocalDate birthDate;
    private Integer age;
    private String gender;
    private String phoneNumber;
    private String telegramNickname;
    private String role;
}
