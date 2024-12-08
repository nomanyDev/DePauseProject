package com.NomDev.DePauseProject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
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
    private User psychologist; // Psychologists are also Users

    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    private String status = "Pending";
    private String therapyType;
}

