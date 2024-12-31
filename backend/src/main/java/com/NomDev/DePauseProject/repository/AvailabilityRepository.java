package com.NomDev.DePauseProject.repository;

import com.NomDev.DePauseProject.entity.Availability;
import com.NomDev.DePauseProject.entity.PsychologistDetails;
import com.NomDev.DePauseProject.entity.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByPsychologistIdAndAvailableDateBetween(Long psychologistId, LocalDate startDate, LocalDate endDate);

    List<Availability> findByPsychologistIdAndAvailableDateAndStatus(Long psychologistId, LocalDate availableDate, SlotStatus status);
    List<Availability> findByPsychologistAndAvailableDate(PsychologistDetails psychologist, LocalDate availableDate);
    @Query("SELECT a FROM Availability a WHERE a.psychologist = :psychologist AND a.availableDate = :availableDate AND a.startTime = :startTime")
    Optional<Availability> findByPsychologistAndAvailableDateAndStartTime(
            @Param("psychologist") PsychologistDetails psychologist,
            @Param("availableDate") LocalDate availableDate,
            @Param("startTime") LocalTime startTime);
}

