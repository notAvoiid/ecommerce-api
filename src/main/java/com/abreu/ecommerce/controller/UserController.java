package com.abreu.ecommerce.controller;

import com.abreu.ecommerce.model.dto.user.AuthenticationDTO;
import com.abreu.ecommerce.model.dto.user.LoginResponseDTO;
import com.abreu.ecommerce.model.dto.user.RegisterDTO;
import com.abreu.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDTO data) {
        userService.register(data);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthenticationDTO authenticationDTO) {
        return ResponseEntity.ok(userService.login(authenticationDTO));
    }

}
