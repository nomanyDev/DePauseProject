package com.NomDev.DePauseProject.utils;
import com.NomDev.DePauseProject.dto.UserDTO;
import com.NomDev.DePauseProject.entity.Role;
import com.NomDev.DePauseProject.entity.User;
import com.NomDev.DePauseProject.utils.Utils;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilsTest {

    @Test
    public void testMapUserToDTO() {
        // Создайте тестовый объект User
        User user = new User();
        user.setId(1L);
        user.setFirstName("Vova");
        user.setLastName("Rosinskyi");
        user.setEmail("rosinskyi@nci.ie");
        user.setProfilePhotoUrl("http://justfortest.com/photo.jpg");
        user.setBirthDate(LocalDate.of(1994, 2, 21));
        user.setGender("M");
        user.setPhoneNumber("+123456789");
        user.setTelegramNickname("@n0many");
        user.setRole(Role.USER);

        // Выполните маппинг
        UserDTO userDTO = Utils.mapUserToDTO(user);

        // Проверьте поля
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getFirstName(), userDTO.getFirstName());
        assertEquals(user.getLastName(), userDTO.getLastName());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertEquals(user.getProfilePhotoUrl(), userDTO.getProfilePhotoUrl());
        assertEquals(user.getBirthDate(), userDTO.getBirthDate());
        assertEquals(user.getGender(), userDTO.getGender());
        assertEquals(user.getPhoneNumber(), userDTO.getPhoneNumber());
        assertEquals(user.getTelegramNickname(), userDTO.getTelegramNickname());
        assertEquals(user.getRole().name(), userDTO.getRole());
    }
}
