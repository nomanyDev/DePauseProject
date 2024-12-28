package com.NomDev.DePauseProject.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PsychologistDTO {
    private Long id;
    private String education;
    private String experience;
    private List<String> therapyTypes;
    private Double rating;
    private BigDecimal price;
    private String description;
    private List<String> certificateUrls;
    private String profilePhotoUrl;
}

