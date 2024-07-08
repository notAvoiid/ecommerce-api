package com.abreu.ecommerce.controller;

import com.abreu.ecommerce.model.dto.ProductRequestDTO;
import com.abreu.ecommerce.model.dto.ProductResponseDTO;
import com.abreu.ecommerce.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> saveProduct(@RequestBody ProductRequestDTO data) {
        return ResponseEntity.ok(productService.saveProduct(data));
    }
}
