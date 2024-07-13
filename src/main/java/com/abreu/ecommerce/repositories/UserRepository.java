package com.abreu.ecommerce.repositories;

import com.abreu.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByUsername(String username);

    boolean existsByUsername(String username);

    @Query(value = "SELECT u FROM User u WHERE u.username = :username")
    Optional<User> loadByUsername(String username);
}
