package com.NomDev.DePauseProject.controller;

import com.NomDev.DePauseProject.dto.Response;
import com.NomDev.DePauseProject.service.interfaces.IPsychologistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/psychologists")
public class PsychologistController {

    @Autowired
    private IPsychologistService psychologistService;

    // Получение списка всех психологов (с пагинацией)
    @GetMapping("/list")
    public ResponseEntity<Response> getAllPsychologists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Response response = psychologistService.getAllPsychologists(pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Получение профиля психолога по ID
    @GetMapping("/{id}")
    public ResponseEntity<Response> getPsychologistById(@PathVariable Long id) {
        Response response = psychologistService.getPsychologistById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Редактирование профиля психолога (психолог)
    @PutMapping("/edit-profile")
    @PreAuthorize("hasAuthority('PSYCHOLOGIST')")
    public ResponseEntity<Response> editProfile(
            @RequestBody EditPsychologistRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        Response response = psychologistService.editPsychologistProfile(email, request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Удаление психолога (админ)
    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deletePsychologist(@PathVariable Long id) {
        Response response = psychologistService.deletePsychologist(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}

