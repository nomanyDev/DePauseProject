package com.NomDev.DePauseProject.dto;

import lombok.Data;

@Data
public class EditUserRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
}
