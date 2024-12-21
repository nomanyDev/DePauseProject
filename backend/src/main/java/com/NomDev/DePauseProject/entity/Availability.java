package com.NomDev.DePauseProject.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDate;
import java.util.Objects;


@Entity
@Data
@Table(name = "availability")
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "psychologist_id", nullable = false)
    private PsychologistDetails psychologist;

    private LocalDate availableDate;

    public Availability() {}

    public Availability(PsychologistDetails psychologist, LocalDate availableDate) {
        this.psychologist = psychologist;
        this.availableDate = availableDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Availability)) return false;
        Availability that = (Availability) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(psychologist, that.psychologist) &&
                Objects.equals(availableDate, that.availableDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, psychologist, availableDate);
    }
}
