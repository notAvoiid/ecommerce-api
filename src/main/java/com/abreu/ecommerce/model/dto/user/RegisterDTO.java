package com.abreu.ecommerce.model.dto.user;

public record RegisterDTO(
        String name,
        String username,
        String CPF,
        String email,
        String password
) {
}
