package com.NomDev.DePauseProject.service.impl;

import com.NomDev.DePauseProject.dto.*;
import com.NomDev.DePauseProject.entity.Role;
import com.NomDev.DePauseProject.entity.User;
import com.NomDev.DePauseProject.exception.OurException;
import com.NomDev.DePauseProject.repository.PsychologistDetailsRepository;
import com.NomDev.DePauseProject.repository.UserRepository;
import com.NomDev.DePauseProject.service.interfaces.IUserService;
import com.NomDev.DePauseProject.utils.JWTUtils;
import com.NomDev.DePauseProject.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.NomDev.DePauseProject.entity.PsychologistDetails;

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
    @Autowired
    private PsychologistDetailsRepository psychologistRepository;

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
    public Response getMyInfo(String email) {
        Response response = new Response();
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new OurException("User not found"));

            //user info
            UserDTO userDTO = Utils.mapUserToDTO(user);

            //if psychology
            PsychologistDTO psychologistDTO = null;
            if (user.getRole() == Role.PSYCHOLOGIST) {
                PsychologistDetails psychologistDetails = psychologistRepository.findByUserId(user.getId())
                        .orElse(null);
                if (psychologistDetails != null) {
                    psychologistDTO = Utils.mapPsychologistToDTO(psychologistDetails);
                }
            }

            //response
            response.setStatusCode(200);
            response.setMessage("User info fetched successfully");
            response.setUser(userDTO);
            response.setPsychologist(psychologistDTO); // Добавляем психолога, если есть

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching user info: " + e.getMessage());
        }
        return response;
    }
    @Override
    public Response editUser(Long userId, EditUserRequest editUserRequest) {
        Response response = new Response();
        try {
            //check
            System.out.println("Edit User Request initiated");
            System.out.println("User ID to edit: " + userId);
            System.out.println("Edit Request Data: " + editUserRequest);
            System.out.println("Logged-in user email: " + SecurityContextHolder.getContext().getAuthentication().getName());
            System.out.println("Logged-in user roles: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());

            //id
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User not found"));

            //auth
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loggedInEmail = authentication.getName();

            // if not admin, edit only presonal profile
            if (!user.getEmail().equals(loggedInEmail) && authentication.getAuthorities().stream()
                    .noneMatch(authority -> authority.getAuthority().equals("ADMIN"))) {
                throw new OurException("Access denied");
            }


            if (editUserRequest.getFirstName() != null) {
                user.setFirstName(editUserRequest.getFirstName());
            }
            if (editUserRequest.getLastName() != null) {
                user.setLastName(editUserRequest.getLastName());
            }
            if (editUserRequest.getPhoneNumber() != null) {
                user.setPhoneNumber(editUserRequest.getPhoneNumber());
            }
            if (editUserRequest.getEmail() != null && !user.getEmail().equals(editUserRequest.getEmail())) {
                if (userRepository.existsByEmail(editUserRequest.getEmail())) {
                    throw new OurException("Email already in use");
                }
                user.setEmail(editUserRequest.getEmail());
            }
            if (editUserRequest.getBirthDate() != null) {
                user.setBirthDate(editUserRequest.getBirthDate());
            }

            // if admin, can change role
            if (editUserRequest.getRole() != null && authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ADMIN"))) {
                user.setRole(Role.valueOf(editUserRequest.getRole().toUpperCase()));
            }

            // save
            User updatedUser = userRepository.save(user);

            UserDTO userDTO = Utils.mapUserToDTO(updatedUser);
            response.setStatusCode(200);
            response.setMessage("User updated successfully");
            response.setUser(userDTO);

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating user: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response editUserRole(Long userId, String role) {
        Response response = new Response();
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User not found"));

            if (role != null) {
                user.setRole(Role.valueOf(role.toUpperCase()));
            } else {
                throw new OurException("Role cannot be null");
            }

            User updatedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserToDTO(updatedUser);
            response.setStatusCode(200);
            response.setMessage("User role updated successfully");
            response.setUser(userDTO);

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating user role: " + e.getMessage());
        }
        return response;
    }
    @Override
    public Response changePassword(ChangePasswordRequest request, String email) {
        Response response = new Response();
        try {
            var user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new OurException("User not found"));

            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new OurException("Current password is incorrect");
            }

            if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
                throw new OurException("New password and confirmation password do not match");
            }

            // change pass
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);

            // response
            response.setStatusCode(200);
            response.setMessage("Password updated successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred during password update: " + e.getMessage());
        }
        return response;
    }

}



