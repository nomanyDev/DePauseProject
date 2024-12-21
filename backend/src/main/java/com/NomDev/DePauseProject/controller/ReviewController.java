package com.NomDev.DePauseProject.controller;

import com.NomDev.DePauseProject.dto.Response;
import com.NomDev.DePauseProject.dto.ReviewDTO;
import com.NomDev.DePauseProject.service.interfaces.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private IReviewService reviewService;


    @GetMapping("/psychologist/{psychologistId}")
    public ResponseEntity<Response> getReviewsByPsychologistId(
            @PathVariable Long psychologistId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewDTO> reviews = reviewService.getReviewsByPsychologistId(psychologistId, pageable);
        Response response = new Response();
        response.setStatusCode(200);
        response.setMessage("Reviews fetched successfully");
        response.setPage(reviews);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/create")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        ReviewDTO createdReview = reviewService.createReview(reviewDTO);
        return ResponseEntity.status(201).body(createdReview);
    }
    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReviewByAdmin(reviewId);
        Response response = new Response();
        response.setStatusCode(200);
        response.setMessage("Review deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/psychologist/{psychologistId}/average-rating")
    public ResponseEntity<Response> getAverageRating(@PathVariable Long psychologistId) {
        Double averageRating = reviewService.calculateAverageRating(psychologistId);
        Response response = new Response();
        response.setStatusCode(200);
        response.setMessage("Average rating fetched successfully");
        response.setRole(String.valueOf(averageRating));
        return ResponseEntity.ok(response);
    }
}

