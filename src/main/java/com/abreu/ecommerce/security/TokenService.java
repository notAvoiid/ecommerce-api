package com.abreu.ecommerce.security;

import com.abreu.ecommerce.exceptions.NullUserException;
import com.abreu.ecommerce.model.User;
import com.abreu.ecommerce.repositories.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
public class TokenService {

    @Autowired
    private UserRepository userRepository;

    @Value("${api.security.token.secret}")
    private String secretKey;
    private Algorithm algorithm;


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public String generateToken(User data) {
        try {
            return JWT.create()
                    .withClaim("role", data.getRole().toString())
                    .withIssuer("ecommerce")
                    .withSubject(data.getUsername())
                    .withIssuedAt(new Date().toInstant())
                    .withExpiresAt(Date.from(getExpirationDate()))
                    .sign(algorithm);
        } catch (JWTCreationException ex) {
            throw new JWTCreationException("Error while generating token. Try again later!", ex);
        }
    }

    public boolean validateToken(String token) {
        try {
            algorithm = Algorithm.HMAC256(secretKey.getBytes());
            JWT.require(algorithm)
                    .withIssuer("ecommerce")
                    .build()
                    .verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return JWT.require(algorithm)
                .withIssuer("ecommerce")
                .build()
                .verify(token)
                .getSubject();
    }

    public String getRoleFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer("ecommerce")
                    .build()
                    .verify(token);
            return jwt.getClaim("role").asString();
        } catch (JWTVerificationException ex) {
            throw new JWTVerificationException("Token verification failed. Please authenticate again.", ex);
        }
    }

    public Instant getExpirationDateFromToken(String token) {
        return JWT.require(algorithm)
                .withIssuer("ecommerce")
                .build()
                .verify(token)
                .getExpiresAt()
                .toInstant();
    }

    public Optional<User> getAuthUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            return userRepository.loadByUsername(username);
        } else
            throw new NullUserException();
    }

    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}