package com.NomDev.DePauseProject.repository;

import com.NomDev.DePauseProject.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByPsychologistId(Long psychologistId, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.psychologist.id = :psychologistId")
    Double calculateAverageRatingByPsychologistId(@Param("psychologistId") Long psychologistId);
    boolean existsByUserIdAndPsychologistId(Long userId, Long psychologistId);

}
