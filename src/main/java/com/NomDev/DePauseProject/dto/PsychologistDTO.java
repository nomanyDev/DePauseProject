package com.NomDev.DePauseProject.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PsychologistDTO {
    private Long id;
    private String name;
    private String education;
    private String experience;
    private List<String> therapyTypes; // Типы терапии
    private Double rating; // Средний рейтинг
    private BigDecimal price; // Стоимость приема
    private String description; // Описание
    private List<String> certificates; // Список URL сертификатов
    private String profilePhotoUrl; // URL фотографии профиля (из User)
}
