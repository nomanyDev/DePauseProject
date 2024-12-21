package com.NomDev.DePauseProject.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewDTO {
    private Long id;
    @Size(max = 400, message = "Review content max 400 characters")
    private String content;
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;
    private Long appointmentId;
    private Long userId;
    private Long psychologistId;
}
