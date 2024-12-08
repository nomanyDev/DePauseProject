package com.NomDev.DePauseProject.controller;

import com.NomDev.DePauseProject.entity.User;
import com.NomDev.DePauseProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/psychologists")
public class PsychologistController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<User>> getAllPsychologists() {
        List<User> psychologists = userRepository.findAllByRole("PSYCHOLOGIST");
        return ResponseEntity.ok(psychologists);
    }
}

