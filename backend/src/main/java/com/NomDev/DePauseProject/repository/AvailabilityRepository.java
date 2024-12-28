package com.NomDev.DePauseProject.repository;

import com.NomDev.DePauseProject.entity.Availability;
import com.NomDev.DePauseProject.entity.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByPsychologistIdAndAvailableDateBetween(Long psychologistId, LocalDate startDate, LocalDate endDate);

    List<Availability> findByPsychologistIdAndAvailableDateAndStatus(Long psychologistId, LocalDate availableDate, SlotStatus status);
}

