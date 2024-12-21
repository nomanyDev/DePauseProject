package com.NomDev.DePauseProject.controller;

import com.NomDev.DePauseProject.dto.ChangePasswordRequest;
import com.NomDev.DePauseProject.dto.EditUserRequest;
import com.NomDev.DePauseProject.dto.Response;
import com.NomDev.DePauseProject.service.UserProfilePhotoService;
import com.NomDev.DePauseProject.service.interfaces.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserProfilePhotoService userProfilePhotoService;


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

    @PutMapping("/edit")
    public ResponseEntity<Response> editUser(
            @RequestParam("id") Long id,
            @RequestBody EditUserRequest editUserRequest) {
        Response response = userService.editUser(id, editUserRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteUser(@PathVariable Long id) {
        Response response = userService.deleteUser(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<Response> getProfileInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Response response = userService.getMyInfo(email);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PutMapping("/{id}/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> editUserRole(@PathVariable Long id, @RequestParam String role) {
        Response response = userService.editUserRole(id, role);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PutMapping("/change-password")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('PSYCHOLOGIST')")
    public ResponseEntity<Response> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        Response response = userService.changePassword(request, email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PostMapping("/{userId}/upload-profile-photo")
    public ResponseEntity<String> uploadProfilePhoto(
            @PathVariable Long userId,
            @RequestParam("photo") MultipartFile photo
    ) {
        String photoUrl = userProfilePhotoService.uploadProfilePhoto(userId, photo);
        return ResponseEntity.ok(photoUrl);
    }
}
