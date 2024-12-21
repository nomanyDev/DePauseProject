package com.NomDev.DePauseProject.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class EditPsychologistRequest {
    private String education;
    private String experience;
    private List<String> therapyTypes;
    private BigDecimal price;
    private Double rating;
    private String description;
    private List<String> certificateUrls;
}

