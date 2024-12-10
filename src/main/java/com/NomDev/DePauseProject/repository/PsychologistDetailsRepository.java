package com.NomDev.DePauseProject.repository;

import com.NomDev.DePauseProject.entity.PsychologistDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PsychologistDetailsRepository extends JpaRepository<PsychologistDetails, Long> {
    Page<PsychologistDetails> findAll(Pageable pageable);
    List<PsychologistDetails> findByTherapyTypesContaining(String therapyType);
}
