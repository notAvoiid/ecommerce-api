package com.abreu.ecommerce.service;

import com.abreu.ecommerce.exceptions.NullProductException;
import com.abreu.ecommerce.model.Order;
import com.abreu.ecommerce.model.Product;
import com.abreu.ecommerce.model.User;
import com.abreu.ecommerce.model.dto.order.OrderRequestDTO;
import com.abreu.ecommerce.model.dto.order.OrderResponseDTO;
import com.abreu.ecommerce.repositories.OrderRepository;
import com.abreu.ecommerce.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final TokenService tokenService;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, TokenService tokenService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public void addToCart(OrderRequestDTO data) {
        if(data.quantity() < 1) throw new RuntimeException();
        Optional<User> optionalUser = tokenService.getAuthUser();
        optionalUser.ifPresent(user -> {
            Optional<Product> optionalProduct = productRepository.findById(data.productId());
            if(optionalProduct.isPresent() && optionalProduct.get().isActive()) {
                Product product = optionalProduct.get();
                Optional<Order> optionalOrder = Optional.empty();
                for (Order order : product.getOrders())
                    if(order.isCompleted() && user.getActiveCart().contains(order))
                        optionalOrder = Optional.of(order);
                optionalOrder.ifPresentOrElse(
                        order -> {
                            int quantity = order.getQuantity() + data.quantity();
                            order.setQuantity(quantity);
                            order.setPrice(quantity * order.getProduct().getPrice());
                            orderRepository.save(order);
                        }, () -> orderRepository.save(
                                new Order(product, data.quantity(), product.getPrice() * data.quantity(), user)));
            } else
                throw new NullProductException();
        });
    }

    @Transactional(readOnly = true)
    public Set<OrderResponseDTO> getUserCart() {
        Optional<User> optionalUser = tokenService.getAuthUser();
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getActiveCart().stream()
                    .map(OrderResponseDTO::new).collect(Collectors.toSet());
        } else
            throw new RuntimeException();
    }

}
