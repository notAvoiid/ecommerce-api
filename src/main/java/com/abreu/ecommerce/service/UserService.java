package com.abreu.ecommerce.service;

import com.abreu.ecommerce.model.User;
import com.abreu.ecommerce.model.dto.user.AuthenticationDTO;
import com.abreu.ecommerce.model.dto.user.LoginResponseDTO;
import com.abreu.ecommerce.model.dto.user.RegisterDTO;
import com.abreu.ecommerce.repositories.UserRepository;
import com.abreu.ecommerce.security.TokenService;
import com.abreu.ecommerce.service.utils.CPFValidator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService{

    private final ApplicationContext context;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder encoder;
    private final CPFValidator validator;

    public UserService(ApplicationContext context, UserRepository userRepository, TokenService tokenService, PasswordEncoder encoder, CPFValidator validator) {
        this.context = context;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.encoder = encoder;
        this.validator = validator;
    }

    @Transactional
    public LoginResponseDTO login(AuthenticationDTO data) {

        UserDetails user = userRepository.findByUsername(data.username());
        if (user == null || !encoder.matches(data.password(), user.getPassword())) throw new RuntimeException();

        AuthenticationManager authenticationManager = context.getBean(AuthenticationManager.class);
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        log.info("An user is authenticated!");

        return new LoginResponseDTO(auth.getName(), token);
    }

    @Transactional
    public void register(RegisterDTO data) {
        if (!validator.validate(data.CPF())) throw new RuntimeException();
        if (this.userRepository.existsByUsernameOrCPFOrEmail(data.username(), data.CPF(), data.email())) throw new RuntimeException();
        User newUser = new User(data.name(), data.username(), data.CPF(), data.email(), encoder.encode(data.password()));
        this.userRepository.save(newUser);

        log.info("Registered an user!!");
    }

}