package com.abreu.ecommerce.controller;

import com.abreu.ecommerce.model.dto.address.AddressRequestDTO;
import com.abreu.ecommerce.model.dto.address.AddressResponseDTO;
import com.abreu.ecommerce.model.dto.address.AddressUpdateDTO;
import com.abreu.ecommerce.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<Set<AddressResponseDTO>> findUserNumber() {
        return ResponseEntity.ok(addressService.findUserAddresses());
    }

    @PostMapping
    public ResponseEntity<Void> saveNumber(@RequestBody AddressRequestDTO data) {
        addressService.saveAddress(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateNumber(@RequestBody AddressUpdateDTO data) {
        addressService.updateAddress(data);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNumber(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

}
