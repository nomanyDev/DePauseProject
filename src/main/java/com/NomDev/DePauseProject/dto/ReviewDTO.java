package com.NomDev.DePauseProject.dto;

import lombok.Data;

@Data
public class ReviewDTO {
    private Long id;
    private String content; // Текст отзыва
    private Integer rating; // Рейтинг
    private Long appointmentId; // ID записи
    private UserDTO user; // Информация о клиенте, оставившем отзыв
    private PsychologistDTO psychologist; // Информация о психологе
}
