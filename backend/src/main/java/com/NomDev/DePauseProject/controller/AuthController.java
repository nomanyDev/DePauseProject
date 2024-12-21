package com.NomDev.DePauseProject.controller;

import com.NomDev.DePauseProject.dto.LoginRequest;
import com.NomDev.DePauseProject.dto.RegisterRequest;
import com.NomDev.DePauseProject.dto.Response;
import com.NomDev.DePauseProject.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserService userService;


    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody RegisterRequest registerRequest) {
        Response response = userService.register(registerRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        Response response = userService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PostMapping("/register-psychologist")
    public ResponseEntity<Response> registerPsychologist(@RequestBody RegisterRequest registerRequest) {
        registerRequest.setRole("PSYCHOLOGIST");
        Response response = userService.register(registerRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}




