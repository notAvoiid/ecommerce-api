package com.abreu.ecommerce.repositories;

import com.abreu.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    boolean existsByUsernameOrCPFOrEmail(String username, String CPF, String email);

    @Query(value = "SELECT u FROM User u WHERE u.username = :username")
    Optional<User> loadByUsername(String username);
}
