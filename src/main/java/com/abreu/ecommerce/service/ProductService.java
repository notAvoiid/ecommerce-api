package com.abreu.ecommerce.service;

import com.abreu.ecommerce.exceptions.ProductNotFoundException;
import com.abreu.ecommerce.model.Product;
import com.abreu.ecommerce.model.dto.ProductRequestDTO;
import com.abreu.ecommerce.model.dto.ProductResponseDTO;
import com.abreu.ecommerce.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO findProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException(String.format("id:%s not found!", id))
        );
        return new ProductResponseDTO(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAllProducts() {
        var products = productRepository.findAll();
        return products.stream()
                .filter(Product::isActive)
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponseDTO saveProduct(ProductRequestDTO data) {
        if (data.price() <= 0) throw new RuntimeException();

        var newProduct = new Product(data);
        return new ProductResponseDTO(productRepository.save(newProduct));
    }
}
