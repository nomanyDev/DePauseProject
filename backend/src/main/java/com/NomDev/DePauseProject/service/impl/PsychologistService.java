package com.NomDev.DePauseProject.service.impl;

import com.NomDev.DePauseProject.dto.PsychologistDTO;
import com.NomDev.DePauseProject.dto.Response;
import com.NomDev.DePauseProject.dto.EditPsychologistRequest;
import com.NomDev.DePauseProject.entity.PsychologistDetails;
import com.NomDev.DePauseProject.entity.TherapyType;
import com.NomDev.DePauseProject.entity.User;
import com.NomDev.DePauseProject.entity.Review;
import com.NomDev.DePauseProject.exception.OurException;
import com.NomDev.DePauseProject.repository.PsychologistDetailsRepository;
import com.NomDev.DePauseProject.repository.ReviewRepository;
import com.NomDev.DePauseProject.repository.UserRepository;
import com.NomDev.DePauseProject.service.interfaces.IPsychologistService;
import com.NomDev.DePauseProject.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PsychologistService implements IPsychologistService {

    @Autowired
    private PsychologistDetailsRepository psychologistDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Response getAllPsychologists(Pageable pageable) {
        Response response = new Response();
        try {
            Page<PsychologistDetails> psychologistsPage = psychologistDetailsRepository.findAll(pageable);
            Page<PsychologistDTO> psychologistDTOs = psychologistsPage.map(Utils::mapPsychologistToDTO);

            response.setStatusCode(200);
            response.setMessage("Psychologists fetched successfully");
            response.setPage(psychologistDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching psychologists: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getPsychologistById(Long id) {
        Response response = new Response();
        try {
            PsychologistDetails psychologistDetails = psychologistDetailsRepository.findById(id)
                    .orElseThrow(() -> new OurException("Psychologist not found"));
            PsychologistDTO psychologistDTO = Utils.mapPsychologistToDTO(psychologistDetails);

            response.setStatusCode(200);
            response.setMessage("Psychologist fetched successfully");
            response.setPsychologist(psychologistDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching psychologist: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response editPsychologistProfile(String email, EditPsychologistRequest request) {
        Response response = new Response();
        try {

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new OurException("Psychologist not found"));


            PsychologistDetails details = user.getPsychologistDetails();
            if (details == null) {

                details = new PsychologistDetails();
                details.setUser(user);
                user.setPsychologistDetails(details);
            }


            if (request.getEducation() != null) details.setEducation(request.getEducation());
            if (request.getExperience() != null) details.setExperience(request.getExperience());
            if (request.getTherapyTypes() != null) details.setTherapyTypes(
                    request.getTherapyTypes().stream().map(TherapyType::valueOf).toList());
            if (request.getPrice() != null) details.setPrice(request.getPrice());
            if (request.getDescription() != null) details.setDescription(request.getDescription());


            Double averageRating = reviewRepository.findByPsychologistId(details.getId(), Pageable.unpaged())
                    .getContent()
                    .stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            details.setRating(averageRating);


            psychologistDetailsRepository.save(details);

            response.setStatusCode(200);
            response.setMessage("Profile updated successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating profile: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deletePsychologist(Long id) {
        Response response = new Response();
        try {
            PsychologistDetails psychologistDetails = psychologistDetailsRepository.findById(id)
                    .orElseThrow(() -> new OurException("Psychologist not found"));

            psychologistDetailsRepository.delete(psychologistDetails);

            response.setStatusCode(200);
            response.setMessage("Psychologist deleted successfully");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting psychologist: " + e.getMessage());
        }
        return response;
    }
    @Override
    public Response searchPsychologistsByName(String name) {
        Response response = new Response();
        try {
            List<PsychologistDetails> psychologists = psychologistDetailsRepository.searchByName(name);
            List<PsychologistDTO> psychologistDTOs = psychologists.stream()
                    .map(Utils::mapPsychologistToDTO)
                    .toList();

            response.setStatusCode(200);
            response.setMessage("Psychologists found successfully");
            response.setPsychologistList(psychologistDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error searching psychologists: " + e.getMessage());
        }
        return response;
    }
}
