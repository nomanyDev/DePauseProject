package com.NomDev.DePauseProject.service.impl;

import com.NomDev.DePauseProject.dto.LoginRequest;
import com.NomDev.DePauseProject.dto.RegisterRequest;
import com.NomDev.DePauseProject.dto.Response;
import com.NomDev.DePauseProject.dto.UserDTO;
import com.NomDev.DePauseProject.entity.Role;
import com.NomDev.DePauseProject.entity.User;
import com.NomDev.DePauseProject.exception.OurException;
import com.NomDev.DePauseProject.repository.UserRepository;
import com.NomDev.DePauseProject.service.interfaces.IUserService;
import com.NomDev.DePauseProject.utils.JWTUtils;
import com.NomDev.DePauseProject.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                throw new OurException("Email already exists");
            }

            String role = registerRequest.getRole();
            if (!role.equals("USER") && !role.equals("PSYCHOLOGIST")) {
                throw new OurException("Invalid role specified");
            }

            User user = new User();
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setGender(registerRequest.getGender());
            user.setBirthDate(registerRequest.getBirthDate());
            user.setPhoneNumber(registerRequest.getPhoneNumber());
            user.setTelegramNickname(registerRequest.getTelegramNickname());
            user.setRole(Role.valueOf(role.toUpperCase()));

            User savedUser = userRepository.save(user);
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
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            ));

            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new OurException("User not found"));

            String token = jwtUtils.generateToken(user);

            response.setStatusCode(200);
            response.setMessage("Login successful");
            response.setToken(token);
            response.setRole(user.getRole().name());
            response.setExpirationTime("7 Days");

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
    public Response getAllUsers(Pageable pageable) {
        Response response = new Response();
        try {
            Page<User> userPage = userRepository.findAll(pageable);
            Page<UserDTO> userDTOPage = userPage.map(Utils::mapUserToDTO);
            response.setStatusCode(200);
            response.setMessage("Users fetched successfully");
            response.setPage(userDTOPage);

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

    @Override
    public Response updateUserProfile(String email, String firstName, String lastName, String phoneNumber) {
        Response response = new Response();
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new OurException("User not found"));
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhoneNumber(phoneNumber);

            User updatedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserToDTO(updatedUser);

            response.setStatusCode(200);
            response.setMessage("User profile updated successfully");
            response.setUser(userDTO);

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating user profile: " + e.getMessage());
        }
        return response;
    }
}



