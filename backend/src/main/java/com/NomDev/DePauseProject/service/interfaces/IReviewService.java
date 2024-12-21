package com.NomDev.DePauseProject.service.interfaces;

import com.NomDev.DePauseProject.dto.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface IReviewService {
    Page<ReviewDTO> getReviewsByPsychologistId(Long psychologistId, Pageable pageable); // Получение отзывов с пагинацией
    Double calculateAverageRating(Long psychologistId); // Подсчет среднего рейтинга
    void deleteReviewByAdmin(Long reviewId);
    ReviewDTO createReview(ReviewDTO reviewDTO);
}
