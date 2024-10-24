package com.abreu.ecommerce.service;

import com.abreu.ecommerce.exceptions.NullProductException;
import com.abreu.ecommerce.exceptions.ProductNotFoundException;
import com.abreu.ecommerce.model.Cart;
import com.abreu.ecommerce.model.Product;
import com.abreu.ecommerce.model.User;
import com.abreu.ecommerce.model.dto.product.ProductRequestDTO;
import com.abreu.ecommerce.model.dto.product.ProductResponseDTO;
import com.abreu.ecommerce.model.dto.product.ProductUpdateDTO;
import com.abreu.ecommerce.repositories.CartRepository;
import com.abreu.ecommerce.repositories.ProductRepository;
import com.abreu.ecommerce.security.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final TokenService tokenService;

    public ProductService(ProductRepository productRepository, CartRepository cartRepository, TokenService tokenService) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.tokenService = tokenService;
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
            if (product.getCarts().stream().anyMatch(Cart::isCompleted)) {
                product.getCarts().stream()
                        .filter(order -> !order.isCompleted())
                        .forEach(order -> {
                            cartRepository.delete(order);
                            product.getCarts().remove(order);
                        });
                product.setActive(false);
                productRepository.save(product);
                log.warn("A product was deleted!");
            } else {
                cartRepository.deleteAll(product.getCarts());
                productRepository.delete(product);
            }
        } else {
            throw new NullPointerException();
        }
    }

    public Set<ProductResponseDTO> getUserWishlist() {
        Optional<User> optionalUser = tokenService.getAuthUser();
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getWishlist().stream()
                    .map(ProductResponseDTO::new).collect(Collectors.toSet());
        } else
            return null;
    }

    public void favorite(Long id) {
        Optional<User> optionalUser = tokenService.getAuthUser();
        optionalUser.ifPresent(user -> {
            Optional<Product> optionalProduct = productRepository.findById(id);
            if(optionalProduct.isPresent() && optionalProduct.get().isActive()) {
                var product = optionalProduct.get();
                Set<Product> wishlist = user.getWishlist();
                Set<User> userList = product.getUsers();
                if(!wishlist.contains(product)) {
                    wishlist.add(product);
                    userList.add(user);
                    productRepository.save(product);
                } else
                    throw new RuntimeException("The product was already favorited!");
            } else
                throw new NullProductException();
        });
    }
    public void unfavorite(Long id) {
        Optional<User> optionalUser = tokenService.getAuthUser();
        optionalUser.ifPresent(user -> {
            Optional<Product> optionalProduct = productRepository.findById(id);
            if(optionalProduct.isPresent() && optionalProduct.get().isActive()) {
                var product = optionalProduct.get();
                Set<Product> wishlist = user.getWishlist();
                Set<User> userList = product.getUsers();
                if(wishlist.contains(product)) {
                    wishlist.remove(product);
                    userList.remove(user);
                    productRepository.save(product);
                } else
                    throw new RuntimeException("The product hasn't been favorited yet!");
            } else
                throw new NullProductException();
        });
    }

}
