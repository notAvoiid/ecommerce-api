package com.abreu.ecommerce.model.dto.user;

import com.abreu.ecommerce.model.enums.UserRole;

public record RegisterDTO(
        String name,
        String username,
        String password,
        UserRole role
) {
}
