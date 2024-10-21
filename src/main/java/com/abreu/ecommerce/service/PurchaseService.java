package com.abreu.ecommerce.service;

import com.abreu.ecommerce.model.Address;
import com.abreu.ecommerce.model.Order;
import com.abreu.ecommerce.model.Purchase;
import com.abreu.ecommerce.model.User;
import com.abreu.ecommerce.model.dto.purchase.PurchaseRequestDTO;
import com.abreu.ecommerce.model.dto.purchase.PurchaseResponseDTO;
import com.abreu.ecommerce.repositories.AddressRepository;
import com.abreu.ecommerce.repositories.OrderRepository;
import com.abreu.ecommerce.repositories.PurchaseRepository;
import com.abreu.ecommerce.security.TokenService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final TokenService tokenService;

    public PurchaseService(PurchaseRepository purchaseRepository, OrderRepository orderRepository, AddressRepository addressRepository, TokenService tokenService) {
        this.purchaseRepository = purchaseRepository;
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.tokenService = tokenService;
    }

    public Set<PurchaseResponseDTO> findAllUserPurchases() {
        List<Purchase> purchasesList = purchaseRepository.findAll();
        return purchasesList.stream()
                .map(purchase -> new PurchaseResponseDTO(
                        purchase.getId(),
                        purchase.getTotalPrice(),
                        purchase.getOrders(),
                        purchase.getAddress()
                ))
                .collect(Collectors.toSet());
    }

    public Set<PurchaseResponseDTO> findUserPurchases() {
        Optional<User> optionalUser = tokenService.getAuthUser();
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getPurchases().stream()
                    .map(purchase -> new PurchaseResponseDTO(
                            purchase.getId(),
                            purchase.getTotalPrice(),
                            purchase.getOrders(),
                            purchase.getAddress()
                    ))
                    .collect(Collectors.toSet());
        } else
            return null;
    }

    public void buyOrders(PurchaseRequestDTO data) {
        Optional<User> optionalUser = tokenService.getAuthUser();
        optionalUser.ifPresent(user -> {
            Optional<Address> optionalAddress = addressRepository.findById(data.addressId());
            if(optionalAddress.isEmpty() || !user.getActiveAddresses().contains(optionalAddress.get()))
                throw new RuntimeException();

            double totalPrice = 0D;
            Set<Order> orderList = new HashSet<>();
            for (Long id : data.orderIdSet()) {
                Optional<Order> optionalOrder = orderRepository.findById(id);
                if(optionalOrder.isEmpty() || !user.getCart().contains(optionalOrder.get()))
                    throw new RuntimeException("The order doesn't exist in your cart!");
                else {
                    Order order = optionalOrder.get();
                    totalPrice += order.getPrice();
                    order.setCompleted(true);
                    orderList.add(order);
                }
            }
            Purchase purchase = new Purchase(orderList, totalPrice, optionalAddress.get(), user);
            orderList.forEach(order -> order.setPurchase(purchase));
            purchaseRepository.save(purchase);
        });
    }
}
