package com.NomDev.DePauseProject.utils;

import com.NomDev.DePauseProject.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LombokTest {

    @Test
    public void testLombokGettersAndSetters() {

        User user = new User();

        user.setId(1L);
        user.setFirstName("Vova");
        user.setLastName("NCI");
        user.setEmail("voiva@nci.ie");
        user.setBirthDate(LocalDate.of(1994, 2, 21));
        user.setGender("M");

        assertEquals(1L, user.getId());
        assertEquals("Vova", user.getFirstName());
        assertEquals("NCI", user.getLastName());
        assertEquals("voiva@nci.ie", user.getEmail());
        assertEquals(LocalDate.of(1994, 2, 21), user.getBirthDate());
        assertEquals("M", user.getGender());

        assertNotNull(user);
    }

    @Test
    public void testDefaultValues() {

        User user = new User();
        assertNotNull(user);
        assertEquals(null, user.getFirstName());
        assertEquals(null, user.getBirthDate());
    }
}
