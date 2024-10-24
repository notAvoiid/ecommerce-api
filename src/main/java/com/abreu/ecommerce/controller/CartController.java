package com.abreu.ecommerce.controller;

import com.abreu.ecommerce.model.dto.cart.CartRequestDTO;
import com.abreu.ecommerce.model.dto.cart.CartResponseDTO;
import com.abreu.ecommerce.model.dto.cart.CartUpdateDTO;
import com.abreu.ecommerce.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<Void> addToCart(@RequestBody CartRequestDTO data) {
        cartService.addToCart(data);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Set<CartResponseDTO>> getUserCart() {
        return ResponseEntity.ok(cartService.getUserCart());
    }

    @PutMapping
    public ResponseEntity<Void> updateOrder(@RequestBody CartUpdateDTO data) {
        cartService.updateCart(data);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFromCart(@PathVariable Long id) {
        cartService.deleteFromCart(id);
        return ResponseEntity.ok().build();
    }
}
