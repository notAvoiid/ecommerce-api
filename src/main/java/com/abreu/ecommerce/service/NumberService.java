package com.abreu.ecommerce.service;

import com.abreu.ecommerce.model.Number;
import com.abreu.ecommerce.model.User;
import com.abreu.ecommerce.model.dto.number.NumberDTO;
import com.abreu.ecommerce.repositories.NumberRepository;
import com.abreu.ecommerce.security.TokenService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NumberService {

    private final NumberRepository numberRepository;
    private final TokenService tokenService;

    public NumberService(NumberRepository numberRepository, TokenService tokenService) {
        this.numberRepository = numberRepository;
        this.tokenService = tokenService;
    }

    public NumberDTO findUserNumber() {
        Optional<User> optionalUser = tokenService.getAuthUser();
        if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                var number = user.getNumber();
                if (number != null) {
                    return new NumberDTO(
                            number.getAreaCode(),
                            number.getNumber()
                    );
                } else throw new RuntimeException();
        } else return null;
    }

    public void saveNumber(NumberDTO data) {
        Optional<User> optionalUser = tokenService.getAuthUser();
        optionalUser.ifPresent(user -> {
            if(user.getNumber() != null)
                throw new RuntimeException();
            var number = new Number(
                    data.areaCode(),
                    data.number(),
                    user);
            numberRepository.save(number);
        });
    }

    public void updateNumber(NumberDTO data) {
        Optional<User> optionalUser = tokenService.getAuthUser();
        optionalUser.ifPresent(user -> {
            var number = user.getNumber();
            if(number != null) {
                number.setAreaCode(data.areaCode());
                number.setNumber(data.number());
                numberRepository.save(number);
            } else
                throw new RuntimeException();
        });
    }

    public void deleteNumber() {
        Optional<User> optionalUser = tokenService.getAuthUser();
        optionalUser.ifPresent(user -> {
            var number = user.getNumber();
            if(number != null)
                numberRepository.delete(number);
            else
                throw new RuntimeException();
        });
    }
}
