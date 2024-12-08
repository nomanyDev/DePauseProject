package com.NomDev.DePauseProject.service.interfaces;

import com.NomDev.DePauseProject.dto.PsychologistDTO;
import com.NomDev.DePauseProject.dto.Response;

import java.util.List;

public interface IPsychologistService {
    List<PsychologistDTO> getAllPsychologists();
    PsychologistDTO getPsychologistById(Long id);
    void updatePsychologistProfile(Long id, PsychologistDTO psychologistDTO);
    Response deletePsychologist(Long id);
}
