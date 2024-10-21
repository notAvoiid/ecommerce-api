package com.abreu.ecommerce.model.dto.purchase;

import com.abreu.ecommerce.model.Address;
import com.abreu.ecommerce.model.Order;

import java.util.Set;

public record PurchaseResponseDTO(
    Long id,
    Double totalPrice,
    Set<Order> orders,
    Address address
) {
}
