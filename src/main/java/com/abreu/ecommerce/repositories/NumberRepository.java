package com.abreu.ecommerce.repositories;

import com.abreu.ecommerce.model.Number;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NumberRepository extends JpaRepository<Number, Long> {
}
