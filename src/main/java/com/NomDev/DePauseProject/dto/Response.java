package com.NomDev.DePauseProject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Исключает null-поля из JSON-ответа
public class Response {

    private int statusCode; // HTTP-статус ответа
    private String message; // Сообщение, поясняющее результат
    private String token; // JWT-токен для авторизации
    private String role; // Роль пользователя (USER, PSYCHOLOGIST, ADMIN)
    private String expirationTime; // Срок действия токена

    private UserDTO user; // Данные о пользователе
    private PsychologistDTO psychologist; // Данные о психологе
    private AppointmentDTO appointment; // Данные о записи на прием
    private ReviewDTO review; // Данные об отзыве

    private List<UserDTO> userList; // Список пользователей
    private List<PsychologistDTO> psychologistList; // Список психологов
    private List<AppointmentDTO> appointmentList; // Список записей на прием
    private List<ReviewDTO> reviewList; // Список отзывов
}

