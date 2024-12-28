package com.NomDev.DePauseProject.security;

import com.NomDev.DePauseProject.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JWTAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request -> request
                        // endpoints for all
                        .requestMatchers("/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                                "/psychologists/list", "/psychologists/{id}",
                                "/reviews/psychologist/{psychologistId}",
                                "/reviews/psychologist/{psychologistId}/average-rating").permitAll()

                        // endpoints for auth users
                        .requestMatchers("/appointments/update-availability").hasAuthority("PSYCHOLOGIST")
                        .requestMatchers("/appointments/**").hasAnyAuthority("USER", "PSYCHOLOGIST", "ADMIN")
                        .requestMatchers("/reviews/create").hasAnyAuthority("USER", "PSYCHOLOGIST", "ADMIN")
                        .requestMatchers("/users/**").authenticated()

                        // endpoints for psychologist
                        .requestMatchers("/psychologists/edit-profile", "/psychologists/upload-certificate").hasAuthority("PSYCHOLOGIST")

                        // endpoints for admin
                        .requestMatchers("/psychologists/{id}/delete", "/users/all", "/users/{id}/delete", "/users/{id}/role", "/reviews/{reviewId}")
                        .hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

