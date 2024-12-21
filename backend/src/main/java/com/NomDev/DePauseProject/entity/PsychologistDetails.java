package com.NomDev.DePauseProject.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "psychologist_details")
public class PsychologistDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String education;

    private String experience;

    @ElementCollection(targetClass = TherapyType.class)
    @CollectionTable(name = "psychologist_therapy_types", joinColumns = @JoinColumn(name = "psychologist_id"))
    @Enumerated(EnumType.STRING)
    private List<TherapyType> therapyTypes = new ArrayList<>();

    private Double rating;

    private BigDecimal price;

    @ElementCollection
    @CollectionTable(name = "psychologist_certificates", joinColumns = @JoinColumn(name = "psychologist_id"))
    private List<String> certificateUrls = new ArrayList<>();

    @OneToMany(mappedBy = "psychologist", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    private String description; // About psychologist
}


