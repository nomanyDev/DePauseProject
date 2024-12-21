package com.NomDev.DePauseProject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "appointments")
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "psychologist_id", nullable = false)
    private PsychologistDetails psychologist;

    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    private String status = "Pending";
    private String therapyType;



}

