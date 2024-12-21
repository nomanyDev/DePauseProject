package com.NomDev.DePauseProject.repository;

import com.NomDev.DePauseProject.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByUserId(Long userId);
    Page<Appointment> findByUserId(Long userId, Pageable pageable);

    List<Appointment> findByPsychologistId(Long psychologistId);
    Page<Appointment> findByPsychologistId(Long psychologistId, Pageable pageable);

    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.psychologist.id = :psychologistId " +
            "AND a.appointmentTime >= :startTime AND a.appointmentTime < :endTime")
    boolean existsAppointmentConflict(@Param("psychologistId") Long psychologistId,
                                      @Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime);

    @Query("SELECT a FROM Appointment a WHERE " +
            "a.appointmentTime >= :startDateTime AND a.appointmentTime < :endDateTime")
    List<Appointment> findConflictingAppointments(@Param("startDateTime") LocalDateTime startDateTime,
                                                  @Param("endDateTime") LocalDateTime endDateTime);
}

