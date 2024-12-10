package com.NomDev.DePauseProject.controller;

import com.NomDev.DePauseProject.dto.EditUserRequest;
import com.NomDev.DePauseProject.dto.Response;
import com.NomDev.DePauseProject.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    // Получить всех пользователей (для ADMIN с пагинацией)
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Response response = userService.getAllUsers(pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Редактирование данных пользователя (для ADMIN)
    @PutMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> editUser(@PathVariable Long id, @RequestBody EditUserRequest editUserRequest) {
        Response response = userService.editUser(id, editUserRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Удалить пользователя (для ADMIN)
    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteUser(@PathVariable Long id) {
        Response response = userService.deleteUser(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Получить информацию о текущем авторизованном пользователе
    @GetMapping("/profile")
    public ResponseEntity<Response> getProfileInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Response response = userService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Редактирование профиля пользователя (сам пользователь)
    @PutMapping("/profile/edit")
    public ResponseEntity<Response> editProfile(@RequestBody EditUserRequest editUserRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Response response = userService.editProfile(email, editUserRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
