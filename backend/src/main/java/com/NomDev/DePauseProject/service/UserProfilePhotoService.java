package com.NomDev.DePauseProject.service;

import com.NomDev.DePauseProject.entity.User;
import com.NomDev.DePauseProject.exception.OurException;
import com.NomDev.DePauseProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserProfilePhotoService {

    @Autowired
    private AwsS3Service awsS3Service;

    @Autowired
    private UserRepository userRepository;

    public String uploadProfilePhoto(Long userId, MultipartFile photo) {
        try {
            // Проверяем пользователя
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User not found"));

            // Загружаем фото в S3
            String photoUrl = awsS3Service.saveImageToS3(photo);

            // Обновляем профиль пользователя
            user.setProfilePhotoUrl(photoUrl);
            userRepository.save(user);

            return photoUrl;
        } catch (Exception e) {
            throw new OurException("Error uploading profile photo: " + e.getMessage());
        }
    }
}
