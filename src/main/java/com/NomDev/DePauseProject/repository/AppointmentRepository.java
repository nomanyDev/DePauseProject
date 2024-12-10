package com.NomDev.DePauseProject.repository;

import com.NomDev.DePauseProject.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByUserId(Long userId);
    Page<Appointment> findByUserId(Long userId, Pageable pageable);

    List<Appointment> findByPsychologistId(Long psychologistId);
    Page<Appointment> findByPsychologistId(Long psychologistId, Pageable pageable);

}
