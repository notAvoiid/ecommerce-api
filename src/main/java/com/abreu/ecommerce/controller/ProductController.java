package com.abreu.ecommerce.controller;

import com.abreu.ecommerce.model.dto.ProductRequestDTO;
import com.abreu.ecommerce.model.dto.ProductResponseDTO;
import com.abreu.ecommerce.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    private ResponseEntity<ProductResponseDTO> saveProduct(@RequestBody ProductRequestDTO data) {
        return ResponseEntity.ok(productService.saveProduct(data));
    }
}
