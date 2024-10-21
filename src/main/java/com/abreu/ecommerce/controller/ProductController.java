package com.abreu.ecommerce.controller;

import com.abreu.ecommerce.model.dto.product.ProductRequestDTO;
import com.abreu.ecommerce.model.dto.product.ProductResponseDTO;
import com.abreu.ecommerce.model.dto.product.ProductUpdateDTO;
import com.abreu.ecommerce.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> findAll() {
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> saveProduct(@RequestBody ProductRequestDTO data) {
        productService.saveProduct(data);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<ProductResponseDTO> updateProduct(@RequestBody ProductUpdateDTO data) {
        productService.updateProduct(data);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/wishlist/me")
    public ResponseEntity<Set<ProductResponseDTO>> getUserWishlist() {
        var userWishlist = productService.getUserWishlist();
        return ResponseEntity.ok(userWishlist);
    }

    @PostMapping("/wishlist/{id}")
    public ResponseEntity<String> favorite(@PathVariable Long id) {
        productService.favorite(id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/wishlist/{id}")
    public ResponseEntity<Void> unfavorite(@PathVariable Long id) {
        productService.unfavorite(id);
        return ResponseEntity.ok().build();
    }
}
