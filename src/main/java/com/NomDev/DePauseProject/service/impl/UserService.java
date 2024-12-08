package com.NomDev.DePauseProject.service.impl;

import com.NomDev.DePauseProject.dto.LoginRequest;
import com.NomDev.DePauseProject.dto.RegisterRequest;
import com.NomDev.DePauseProject.dto.Response;
import com.NomDev.DePauseProject.dto.UserDTO;
import com.NomDev.DePauseProject.entity.User;
import com.NomDev.DePauseProject.exception.OurException;
import com.NomDev.DePauseProject.repository.UserRepository;
import com.NomDev.DePauseProject.service.interfaces.IUserService;
import com.NomDev.DePauseProject.utils.JWTUtils;
import com.NomDev.DePauseProject.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Response register(RegisterRequest registerRequest) {
        Response response = new Response();
        try {
            // Проверка существования email
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                throw new OurException("Email already exists");
            }

            // Проверка валидности роли
            String role = registerRequest.getRole();
            if (!role.equals("USER") && !role.equals("PSYCHOLOGIST")) {
                throw new OurException("Invalid role specified");
            }

            // Создание нового пользователя
            User user = new User();
            user.setName(registerRequest.getName());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setGender(registerRequest.getGender());
            user.setAge(registerRequest.getAge());
            user.setPhoneNumber(registerRequest.getPhoneNumber());
            user.setTelegramNickname(registerRequest.getTelegramNickname());
            user.setRole(role);

            // Сохранение пользователя
            User savedUser = userRepository.save(user);

            // Формирование DTO для ответа
            UserDTO userDTO = Utils.mapUserToDTO(savedUser);
            response.setStatusCode(200);
            response.setMessage("Registration successful");
            response.setUser(userDTO);

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred during registration: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();
        try {
            // Аутентификация пользователя
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            ));

            // Получение пользователя
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new OurException("User not found"));

            // Генерация токена
            String token = jwtUtils.generateToken(user);

            // Установка данных в Response
            response.setStatusCode(200);
            response.setMessage("Login successful");
            response.setToken(token);
            response.setRole(user.getRole().name());
            response.setExpirationTime("7 Days"); // Информирование о времени действия токена

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error during login: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response = new Response();
        try {
            List<User> userList = userRepository.findAll();
            List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("Users fetched successfully");
            response.setUserList(userDTOList);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all users: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(Long userId) {
        Response response = new Response();
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User not found"));
            UserDTO userDTO = Utils.mapUserToDTO(user);
            response.setStatusCode(200);
            response.setMessage("User fetched successfully");
            response.setUser(userDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching user: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(Long userId) {
        Response response = new Response();
        try {
            userRepository.findById(userId).orElseThrow(() -> new OurException("User not found"));
            userRepository.deleteById(userId);
            response.setStatusCode(200);
            response.setMessage("User deleted successfully");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting user: " + e.getMessage());
        }
        return response;
    }
}



