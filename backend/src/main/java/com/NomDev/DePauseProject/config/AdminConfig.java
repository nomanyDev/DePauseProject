package com.NomDev.DePauseProject.config;

import com.NomDev.DePauseProject.entity.Role;
import com.NomDev.DePauseProject.entity.User;
import com.NomDev.DePauseProject.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class AdminConfig {

    @Bean
    public CommandLineRunner createDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByEmail("nomany@live.ru")) {
                User admin = new User();
                admin.setFirstName("Vova");
                admin.setLastName("Rosinskyi");
                admin.setEmail("nomany@live.ru");
                admin.setPassword(passwordEncoder.encode("123456"));
                admin.setGender("male");
                admin.setBirthDate(LocalDate.parse("1994-02-21"));
                admin.setPhoneNumber("string");
                admin.setTelegramNickname("string");
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);
                System.out.println("Default admin created: nomany@live.ru");
            }
        };
    }
}

