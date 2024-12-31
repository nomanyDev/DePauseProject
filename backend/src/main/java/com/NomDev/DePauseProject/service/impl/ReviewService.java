package com.NomDev.DePauseProject.service.impl;

import com.NomDev.DePauseProject.dto.ReviewDTO;
import com.NomDev.DePauseProject.entity.Appointment;
import com.NomDev.DePauseProject.entity.PsychologistDetails;
import com.NomDev.DePauseProject.entity.Review;
import com.NomDev.DePauseProject.entity.User;
import com.NomDev.DePauseProject.exception.OurException;
import com.NomDev.DePauseProject.repository.AppointmentRepository;
import com.NomDev.DePauseProject.repository.PsychologistDetailsRepository;
import com.NomDev.DePauseProject.repository.ReviewRepository;
import com.NomDev.DePauseProject.repository.UserRepository;
import com.NomDev.DePauseProject.service.interfaces.IReviewService;
import com.NomDev.DePauseProject.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService implements IReviewService {
    @Autowired
    private AppointmentRepository appointmentRepository;


    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PsychologistDetailsRepository psychologistDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Double calculateAverageRating(Long psychologistId) {
        return reviewRepository.calculateAverageRatingByPsychologistId(psychologistId);
    }

    @Override
    public Page<ReviewDTO> getReviewsByPsychologistId(Long psychologistId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByPsychologistId(psychologistId, pageable);
        return reviews.map(Utils::mapReviewToDTO);
    }

    @Override
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        // Проверяем, существует ли отзыв для данного сеанса
        boolean reviewExistsForAppointment = reviewRepository.existsByAppointmentId(reviewDTO.getAppointmentId());
        if (reviewExistsForAppointment) {
            throw new OurException("You have already submitted a review for this appointment");
        }

        // Проверяем, существует ли сеанс
        Appointment appointment = appointmentRepository.findById(reviewDTO.getAppointmentId())
                .orElseThrow(() -> new OurException("Appointment not found"));

        // Проверяем, что пользователь совпадает с пользователем сеанса
        if (!appointment.getUser().getId().equals(reviewDTO.getUserId())) {
            throw new OurException("User does not match the appointment");
        }

        // Проверяем, что психолог совпадает с психологом сеанса
        if (!appointment.getPsychologist().getId().equals(reviewDTO.getPsychologistId())) {
            throw new OurException("Psychologist does not match the appointment");
        }

        // Создаем новый отзыв
        Review review = new Review();
        review.setContent(reviewDTO.getContent());
        review.setRating(reviewDTO.getRating());
        review.setAppointment(appointment);

        PsychologistDetails psychologist = psychologistDetailsRepository.findById(reviewDTO.getPsychologistId())
                .orElseThrow(() -> new OurException("Psychologist not found"));
        review.setPsychologist(psychologist);

        User user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new OurException("User not found"));
        review.setUser(user);

        reviewRepository.save(review);

        // Обновляем средний рейтинг для психолога
        Double averageRating = calculateAverageRating(psychologist.getId());
        psychologist.setRating(averageRating);
        psychologistDetailsRepository.save(psychologist);

        return Utils.mapReviewToDTO(review);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteReviewByAdmin(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new OurException("Review not found");
        }
        reviewRepository.deleteById(reviewId);
    }
}

