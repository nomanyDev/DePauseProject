package com.NomDev.DePauseProject.utils;

import com.NomDev.DePauseProject.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Test
    public void testSendEmail() {
        notificationService.sendEmail("nomany@live.ru", "Hello tester=)" +
                " ",
                "Test Emails");
        System.out.println("Email for dev");
    }
}

