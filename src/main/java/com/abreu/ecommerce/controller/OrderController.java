package com.abreu.ecommerce.controller;

import com.abreu.ecommerce.model.dto.order.OrderRequestDTO;
import com.abreu.ecommerce.model.dto.order.OrderResponseDTO;
import com.abreu.ecommerce.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> saveOrder(@RequestBody OrderRequestDTO data) {
        orderService.addToCart(data);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Set<OrderResponseDTO>> getUserCart() {
        return ResponseEntity.ok(orderService.getUserCart());
    }
}
