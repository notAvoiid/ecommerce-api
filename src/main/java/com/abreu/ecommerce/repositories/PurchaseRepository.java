package com.abreu.ecommerce.repositories;

import com.abreu.ecommerce.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
