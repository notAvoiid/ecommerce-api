package com.abreu.ecommerce.model.dto.purchase;

import java.util.Set;

public record PurchaseRequestDTO(
    Set<Long> orderIdSet,
    Long addressId
) {
}
