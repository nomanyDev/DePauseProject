package com.NomDev.DePauseProject.controller;

import com.NomDev.DePauseProject.dto.Response;
import com.NomDev.DePauseProject.entity.PsychologistDetails;
import com.NomDev.DePauseProject.entity.User;
import com.NomDev.DePauseProject.exception.OurException;
import com.NomDev.DePauseProject.repository.PsychologistDetailsRepository;
import com.NomDev.DePauseProject.repository.UserRepository;
import com.NomDev.DePauseProject.service.AwsS3Service;
import com.NomDev.DePauseProject.service.UserProfilePhotoService;
import com.NomDev.DePauseProject.service.interfaces.IPsychologistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.NomDev.DePauseProject.dto.EditPsychologistRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/psychologists")
public class PsychologistController {

    @Autowired
    private IPsychologistService psychologistService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AwsS3Service awsS3Service;

    @Autowired
    private PsychologistDetailsRepository psychologistDetailsRepository;


    @GetMapping("/list")
    public ResponseEntity<Response> getAllPsychologists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Response response = psychologistService.getAllPsychologists(pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Response> getPsychologistById(@PathVariable Long id) {
        Response response = psychologistService.getPsychologistById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PutMapping("/edit-profile")
    @PreAuthorize("hasAuthority('PSYCHOLOGIST')")
    public ResponseEntity<Response> editProfile(
            @RequestBody EditPsychologistRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        System.out.println("Edit Profile Request: " + request); // check
        Response response = psychologistService.editPsychologistProfile(email, request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deletePsychologist(@PathVariable Long id) {
        Response response = psychologistService.deletePsychologist(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PostMapping("/upload-certificate")
    @PreAuthorize("hasAuthority('PSYCHOLOGIST')")
    public ResponseEntity<Response> uploadCertificate(@RequestParam("certificate") MultipartFile certificate, Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new OurException("User not found"));

            if (user.getPsychologistDetails() == null) {
                throw new OurException("Psychologist details not found");
            }

            String certificateUrl = awsS3Service.saveImageToS3(certificate);

            PsychologistDetails details = user.getPsychologistDetails();
            details.getCertificateUrls().add(certificateUrl);
            psychologistDetailsRepository.save(details);

            Response response = new Response();
            response.setStatusCode(200);
            response.setMessage("Certificate uploaded successfully");
            response.setCertificateUrl(certificateUrl);
            return ResponseEntity.ok(response);
        } catch (OurException e) {
            Response response = new Response();
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Response> searchPsychologistsByName(@RequestParam String name) {
        Response response = psychologistService.searchPsychologistsByName(name);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}

