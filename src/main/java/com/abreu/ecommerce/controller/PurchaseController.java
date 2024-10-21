package com.abreu.ecommerce.controller;

import com.abreu.ecommerce.model.dto.purchase.PurchaseRequestDTO;
import com.abreu.ecommerce.model.dto.purchase.PurchaseResponseDTO;
import com.abreu.ecommerce.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public ResponseEntity<Void> buyOrders(@RequestBody PurchaseRequestDTO data) {
        purchaseService.buyOrders(data);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/me")
    public ResponseEntity<Set<PurchaseResponseDTO>> findUserPurchases() {
        var userPurchases = purchaseService.findUserPurchases();
        return ResponseEntity.ok(userPurchases);
    }
    @GetMapping
    public ResponseEntity<Set<PurchaseResponseDTO>> findAllUserPurchases() {
        var purchaseSet = purchaseService.findAllUserPurchases();
        return ResponseEntity.ok(purchaseSet);
    }

}
