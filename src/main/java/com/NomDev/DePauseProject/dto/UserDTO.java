package com.NomDev.DePauseProject.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String profilePhotoUrl; // URL фотографии профиля
    private Integer age;
    private String gender; // "M", "F", "U"
    private String phoneNumber;
    private String telegramNickname;
    private String role; // USER, PSYCHOLOGIST, ADMIN
}
