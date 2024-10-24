package com.abreu.ecommerce.service;

import com.abreu.ecommerce.exceptions.NullCartException;
import com.abreu.ecommerce.exceptions.NullProductException;
import com.abreu.ecommerce.model.Cart;
import com.abreu.ecommerce.model.Product;
import com.abreu.ecommerce.model.User;
import com.abreu.ecommerce.model.dto.cart.CartRequestDTO;
import com.abreu.ecommerce.model.dto.cart.CartResponseDTO;
import com.abreu.ecommerce.model.dto.cart.CartUpdateDTO;
import com.abreu.ecommerce.repositories.CartRepository;
import com.abreu.ecommerce.repositories.ProductRepository;
import com.abreu.ecommerce.security.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final TokenService tokenService;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, TokenService tokenService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.tokenService = tokenService;
    }

    @Transactional(readOnly = true)
    public Set<CartResponseDTO> getUserCart() {
        Optional<User> optionalUser = tokenService.getAuthUser();
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getActiveCart().stream()
                    .map(CartResponseDTO::new).collect(Collectors.toSet());
        } else
            throw new RuntimeException();
    }

    @Transactional
    public void addToCart(CartRequestDTO data) {
        if (data.quantity() < 1) throw new RuntimeException();
        Optional<User> optionalUser = tokenService.getAuthUser();
        optionalUser.ifPresent(user -> {
            Optional<Product> optionalProduct = productRepository.findById(data.productId());
            if (optionalProduct.isPresent() && optionalProduct.get().isActive()) {
                Product product = optionalProduct.get();
                Optional<Cart> optionalCart = Optional.empty();
                for (Cart cart : product.getCarts())
                    if (user.getActiveCart().contains(cart))
                        optionalCart = Optional.of(cart);
                optionalCart.ifPresentOrElse(
                        cart -> {
                            int quantity = cart.getQuantity() + data.quantity();
                            cart.setQuantity(quantity);
                            cart.setPrice(quantity * cart.getProduct().getPrice());
                            cartRepository.save(cart);
                        }, () -> cartRepository.save(
                                new Cart(product, data.quantity(), product.getPrice() * data.quantity(), user)));
            } else
                throw new NullProductException();
        });
    }

    public void updateCart(CartUpdateDTO data) {
        Optional<User> optionalUser = tokenService.getAuthUser();
        optionalUser.ifPresent(user -> {
            Optional<Cart> optionalCart = cartRepository.findById(data.id());
            Optional<Product> optionalProduct = productRepository.findById(data.productId());

            if(optionalCart.isEmpty() || !user.getActiveCart().contains(optionalCart.get()))
                throw new RuntimeException();
            if(optionalProduct.isEmpty())
                throw new NullProductException();
            if(data.quantity() <= 0)
                throw new RuntimeException();

            Cart cart = optionalCart.get();
            Product product = optionalProduct.get();

            cart.setQuantity(data.quantity());
            cart.setProduct(product);
            cart.setPrice(product.getPrice() * data.quantity());

            cartRepository.save(cart);
        });
    }

    @Transactional
    public void deleteFromCart(Long id) {
        Optional<User> optionalUser = tokenService.getAuthUser();
        optionalUser.ifPresent(user -> {
            Optional<Cart> optionalCart = cartRepository.findById(id);
            if(optionalCart.isPresent() && !optionalCart.get().isCompleted() && user.getActiveCart().contains(optionalCart.get())) {
                var cart = optionalCart.get();
                cartRepository.delete(cart);
            } else
                throw new NullCartException("The product doesn't exist in your cart");
        });
    }
}
