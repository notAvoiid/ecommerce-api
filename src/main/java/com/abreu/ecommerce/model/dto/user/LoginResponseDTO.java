package com.abreu.ecommerce.model.dto.user;

public record LoginResponseDTO(
        String username,
        String accessToken
) {
}
