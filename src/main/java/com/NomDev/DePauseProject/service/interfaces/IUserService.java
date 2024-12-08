package com.NomDev.DePauseProject.service.interfaces;

import com.NomDev.DePauseProject.dto.LoginRequest;
import com.NomDev.DePauseProject.dto.RegisterRequest;
import com.NomDev.DePauseProject.dto.Response;


public interface IUserService {
    Response register(RegisterRequest registerRequest);
    Response login(LoginRequest loginRequest);
}
