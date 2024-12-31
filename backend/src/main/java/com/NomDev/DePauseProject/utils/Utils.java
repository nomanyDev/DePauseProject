package com.NomDev.DePauseProject.utils;

import com.NomDev.DePauseProject.dto.AppointmentDTO;
import com.NomDev.DePauseProject.dto.PsychologistDTO;
import com.NomDev.DePauseProject.dto.ReviewDTO;
import com.NomDev.DePauseProject.dto.UserDTO;
import com.NomDev.DePauseProject.entity.*;
import com.NomDev.DePauseProject.repository.ReviewRepository;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generate a random confirmation code of specified length.
     */
    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    /**
     * Map User entity to UserDTO.
     */
    public static UserDTO mapUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setProfilePhotoUrl(user.getProfilePhotoUrl());
        userDTO.setBirthDate(user.getBirthDate());
        userDTO.setAge(user.getAge());
        userDTO.setGender(user.getGender());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setTelegramNickname(user.getTelegramNickname());
        userDTO.setRole(user.getRole().name());
        return userDTO;
    }

    /**
     * Map PsychologistDetails entity to PsychologistDTO.
     */
    public static PsychologistDTO mapPsychologistToDTO(PsychologistDetails details) {
        PsychologistDTO dto = new PsychologistDTO();
        dto.setId(details.getId());
        dto.setEducation(details.getEducation());
        dto.setExperience(details.getExperience());

        List<String> therapyTypeStrings = details.getTherapyTypes().stream()
                .map(TherapyType::toString)
                .collect(Collectors.toList());
        dto.setTherapyTypes(therapyTypeStrings);

        dto.setRating(details.getRating());
        dto.setPrice(details.getPrice());
        dto.setDescription(details.getDescription());
        dto.setCertificateUrls(details.getCertificateUrls());

        if (details.getUser() != null) {
            // Добавляем данные из User
            dto.setProfilePhotoUrl(details.getUser().getProfilePhotoUrl());
            dto.setAge(details.getUser().getAge()); // Используем метод getAge()
            dto.setFirstName(details.getUser().getFirstName());
            dto.setLastName(details.getUser().getLastName());
        } else {
            dto.setProfilePhotoUrl(null);
            dto.setAge(null);
            dto.setFirstName(null);
            dto.setLastName(null);
        }

        return dto;
    }

    /**
     * Map Appointment entity to AppointmentDTO.
     */
    public static AppointmentDTO mapAppointmentToDTO(Appointment appointment, ReviewRepository reviewRepository) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setStatus(appointment.getStatus());
        dto.setTherapyType(appointment.getTherapyType());
        dto.setUser(mapUserToDTO(appointment.getUser()));
        dto.setPsychologist(mapPsychologistToDTO(appointment.getPsychologist()));

        //hasReview for appointment history page
        boolean hasReview = reviewRepository.existsByAppointmentId(appointment.getId());
        dto.setHasReview(hasReview);

        return dto;
    }

    /**
     * Map a list of User entities to a list of UserDTOs.
     */
    public static List<UserDTO> mapUserListToDTOList(List<User> userList) {
        return userList.stream().map(Utils::mapUserToDTO).collect(Collectors.toList());
    }

    /**
     * Map a list of PsychologistDetails entities to a list of PsychologistDTOs.
     */
    public static List<PsychologistDTO> mapPsychologistListToDTOList(List<PsychologistDetails> detailsList) {
        return detailsList.stream()
                .map(Utils::mapPsychologistToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Map a list of Appointment entities to a list of AppointmentDTOs.
     */
    public static List<AppointmentDTO> mapAppointmentListToDTOList(List<Appointment> appointmentList, ReviewRepository reviewRepository) {
        return appointmentList.stream()
                .map(appointment -> mapAppointmentToDTO(appointment, reviewRepository)) // Передаем reviewRepository
                .collect(Collectors.toList());
    }

    public static ReviewDTO mapReviewToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setContent(review.getContent());
        dto.setRating(review.getRating());
        dto.setUserId(review.getUser().getId());
        dto.setPsychologistId(review.getPsychologist().getId());
        dto.setAppointmentId(review.getAppointment().getId());
        dto.setFirstName(review.getUser().getFirstName());
        dto.setLastName(review.getUser().getLastName());
        return dto;
    }
}

