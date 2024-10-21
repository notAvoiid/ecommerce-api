package com.abreu.ecommerce.service;

import com.abreu.ecommerce.exceptions.ProductNotFoundException;
import com.abreu.ecommerce.model.Order;
import com.abreu.ecommerce.model.Product;
import com.abreu.ecommerce.model.dto.product.ProductRequestDTO;
import com.abreu.ecommerce.model.dto.product.ProductResponseDTO;
import com.abreu.ecommerce.model.dto.product.ProductUpdateDTO;
import com.abreu.ecommerce.repositories.OrderRepository;
import com.abreu.ecommerce.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO findProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException(String.format("id:%s not found!", id))
        );
        log.info("Finding a product by his ID");
        return new ProductResponseDTO(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAllProducts() {
        var products = productRepository.findAll();
        log.info("Finding all products");
        return products.stream()
                .filter(Product::isActive)
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveProduct(ProductRequestDTO data) {
        if (data.price() <= 0) throw new RuntimeException();

        var newProduct = new Product(data);
        productRepository.save(newProduct);
        log.info("Saving a product");
    }

    @Transactional
    public void updateProduct(ProductUpdateDTO data) {
        if (data.price() <= 0) throw new RuntimeException();
        Optional<Product> optionalProduct = productRepository.findById(data.id());
        if (optionalProduct.isPresent()) {
            var product = optionalProduct.get();
            product.setName(data.name());
            product.setDescription(data.description());
            product.setPrice(data.price());

            productRepository.save(product);
            log.info("Updating a product");
        } else {
            throw new NullPointerException();
        }
    }

    @Transactional
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent() && optionalProduct.get().isActive()) {
            Product product = optionalProduct.get();
            if (product.getOrders().stream().anyMatch(Order::isCompleted)) {
                product.getOrders().stream()
                        .filter(order -> !order.isCompleted())
                        .forEach(order -> {
                            orderRepository.delete(order);
                            product.getOrders().remove(order);
                        });
                product.setActive(false);
                productRepository.save(product);
                log.warn("A product was deleted!");
            } else {
                orderRepository.deleteAll(product.getOrders());
                productRepository.delete(product);
            }
        } else {
            throw new NullPointerException();
        }
    }

}
