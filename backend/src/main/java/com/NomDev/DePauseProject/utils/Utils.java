package com.NomDev.DePauseProject.utils;

import com.NomDev.DePauseProject.dto.AppointmentDTO;
import com.NomDev.DePauseProject.dto.PsychologistDTO;
import com.NomDev.DePauseProject.dto.ReviewDTO;
import com.NomDev.DePauseProject.dto.UserDTO;
import com.NomDev.DePauseProject.entity.*;

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

        // Преобразование TherapyType в строку
        List<String> therapyTypeStrings = details.getTherapyTypes().stream()
                .map(TherapyType::toString)
                .collect(Collectors.toList());
        dto.setTherapyTypes(therapyTypeStrings);

        dto.setRating(details.getRating());
        dto.setPrice(details.getPrice());
        dto.setDescription(details.getUser().getFirstName() + " " + details.getUser().getLastName());
        dto.setCertificateUrls(details.getCertificateUrls());
        return dto;
    }

    /**
     * Map Appointment entity to AppointmentDTO.
     */
    public static AppointmentDTO mapAppointmentToDTO(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setStatus(appointment.getStatus());
        dto.setTherapyType(appointment.getTherapyType());
        dto.setUser(mapUserToDTO(appointment.getUser()));
        dto.setPsychologist(mapPsychologistToDTO(appointment.getPsychologist())); // Исправлено

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
        return detailsList.stream().map(Utils::mapPsychologistToDTO).collect(Collectors.toList());
    }

    /**
     * Map a list of Appointment entities to a list of AppointmentDTOs.
     */
    public static List<AppointmentDTO> mapAppointmentListToDTOList(List<Appointment> appointmentList) {
        return appointmentList.stream().map(Utils::mapAppointmentToDTO).collect(Collectors.toList());
    }

    public static ReviewDTO mapReviewToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setContent(review.getContent());
        dto.setRating(review.getRating());
        dto.setUserId(review.getUser().getId()); // Устанавливаем ID пользователя
        dto.setPsychologistId(review.getPsychologist().getId()); // Устанавливаем ID психолога
        return dto;
    }
}

