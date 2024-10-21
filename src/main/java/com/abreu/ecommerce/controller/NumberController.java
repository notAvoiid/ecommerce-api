package com.abreu.ecommerce.controller;

import com.abreu.ecommerce.model.dto.number.NumberDTO;
import com.abreu.ecommerce.service.NumberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/number")
public class NumberController {

    private final NumberService numberService;

    public NumberController(NumberService numberService) {
        this.numberService = numberService;
    }

    @GetMapping
    public ResponseEntity<NumberDTO> findUserNumber() {
        return ResponseEntity.ok(numberService.findUserNumber());
    }

    @PostMapping
    public ResponseEntity<Void> saveNumber(@RequestBody NumberDTO data) {
        numberService.saveNumber(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateNumber(@RequestBody NumberDTO data) {
        numberService.updateNumber(data);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteNumber() {
        numberService.deleteNumber();
        return ResponseEntity.noContent().build();
    }
}
