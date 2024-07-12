package com.abreu.ecommerce.service;

import com.abreu.ecommerce.model.User;
import com.abreu.ecommerce.repositories.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;

@Service
public class AuthService {

    @Value("${api.security.token.secret}")
    private String secretKey;
    private Algorithm algorithm;
    private Date now;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
        now = new Date();
    }

    public String generateToken(User data) {
        try {
            return JWT.create()
                    .withClaim("role", String.valueOf(data.getRole()))
                    .withIssuer("ecommerce")
                    .withSubject(data.getUsername())
                    .withIssuedAt(now)
                    .withExpiresAt(getExpirationDate())
                    .sign(algorithm)
                    .strip();
        } catch (JWTCreationException ex) {
            throw new JWTCreationException("Error while generating token. Try again later!", ex);
        }
    }

    public String validateToken(String token) {
        try {
            algorithm = Algorithm.HMAC256(secretKey.getBytes());
            return JWT.require(algorithm)
                    .withIssuer("ecommerce")
                    .build()
                    .verify(token)
                    .getSubject()
                    .strip();
        } catch (JWTVerificationException ex) {
            return "";
        }
    }

    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}