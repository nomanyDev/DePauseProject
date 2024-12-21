package com.NomDev.DePauseProject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "psychologist_id", nullable = false)
    private PsychologistDetails psychologist;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 400)
    private String content;

    @Column(nullable = false)
    private Integer rating;

    /*
    @PrePersist
    private void validateRating() {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }
    @PrePersist
    @PreUpdate
    private void validateContent() {
        if (content.length() > 400) {
            throw new IllegalArgumentException("Review content max 400 characters");
        }
    }
     */
}

