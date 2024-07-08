package com.abreu.ecommerce.model.dto;

import com.abreu.ecommerce.model.Product;

public record ProductResponseDTO(
    Long id,
    String name,
    String description,
    Double price
) {

    public ProductResponseDTO(Product product){
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }

}
