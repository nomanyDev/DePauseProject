package com.NomDev.DePauseProject.controller;

import com.NomDev.DePauseProject.dto.SupportRequest;
import com.NomDev.DePauseProject.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/support")
public class SupportController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<String> sendSupportRequest(@RequestBody SupportRequest request) {
        try {
            notificationService.sendSupportEmail(request.getEmail(), request.getProblemType(), request.getDescription());
            return ResponseEntity.ok("Support request sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send support request.");
        }
    }
}
