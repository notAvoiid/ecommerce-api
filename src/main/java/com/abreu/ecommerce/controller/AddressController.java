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
@RequestMapping("/api/v1/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/me")
    public ResponseEntity<Set<AddressResponseDTO>> findUserAddress() {
        return ResponseEntity.ok(addressService.findUserAddresses());
    }

    @PostMapping
    public ResponseEntity<Void> saveAddress(@RequestBody AddressRequestDTO data) {
        addressService.saveAddress(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateAddress(@RequestBody AddressUpdateDTO data) {
        addressService.updateAddress(data);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

}
