package com.NomDev.DePauseProject.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
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

    private LocalDate availableDate; // Дата

    private LocalTime startTime; // Время начала

    private LocalTime endTime; // Время окончания

    @Enumerated(EnumType.STRING)
    private SlotStatus status; // AVAILABLE, BLOCKED

    public Availability() {}

    public Availability(PsychologistDetails psychologist, LocalDate availableDate) {
        this.psychologist = psychologist;
        this.availableDate = availableDate;
    }

    public Availability(PsychologistDetails psychologist, LocalDate availableDate, LocalTime startTime, LocalTime endTime, SlotStatus status) {
        this.psychologist = psychologist;
        this.availableDate = availableDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Availability)) return false;
        Availability that = (Availability) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(psychologist, that.psychologist) &&
                Objects.equals(availableDate, that.availableDate) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, psychologist, availableDate, startTime, endTime, status);
    }
}
