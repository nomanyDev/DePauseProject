package com.NomDev.DePauseProject.repository;

import com.NomDev.DePauseProject.entity.PsychologistDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PsychologistDetailsRepository extends JpaRepository<PsychologistDetails, Long> {
    @Query("SELECT pd FROM PsychologistDetails pd JOIN pd.user u " +
            "WHERE LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(CONCAT(u.lastName, ' ', u.firstName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<PsychologistDetails> searchByName(@Param("name") String name);

    // Новый метод для поиска по userId
    @Query("SELECT pd FROM PsychologistDetails pd WHERE pd.user.id = :userId")
    Optional<PsychologistDetails> findByUserId(@Param("userId") Long userId);
}


