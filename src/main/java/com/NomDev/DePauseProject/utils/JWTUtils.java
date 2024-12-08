package com.NomDev.DePauseProject.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTUtils {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7 дней
    private static final String STATIC_SECRET_KEY = "YourSuperSecureStaticKey1234567890"; // Ваш токен

    private final SecretKey key;

    public JWTUtils() {
        byte[] keyBytes = Base64.getEncoder().encode(STATIC_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    /**
     * Генерация токена JWT
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Имя пользователя
                .setIssuedAt(new Date(System.currentTimeMillis())) // Время создания
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Время истечения
                .signWith(key, SignatureAlgorithm.HS256) // Подпись с использованием HmacSHA256
                .compact();
    }

    /**
     * Извлечение имени пользователя из токена
     */
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    /**
     * Проверка валидности токена
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Проверка истечения токена
     */
    private boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

    /**
     * Извлечение данных из токена
     */
    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}

