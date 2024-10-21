package com.abreu.ecommerce.service;

import com.abreu.ecommerce.model.Address;
import com.abreu.ecommerce.model.User;
import com.abreu.ecommerce.model.dto.address.AddressRequestDTO;
import com.abreu.ecommerce.model.dto.address.AddressResponseDTO;
import com.abreu.ecommerce.model.dto.address.AddressUpdateDTO;
import com.abreu.ecommerce.repositories.AddressRepository;
import com.abreu.ecommerce.security.TokenService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final TokenService tokenService;

    public AddressService(AddressRepository addressRepository, TokenService tokenService) {
        this.addressRepository = addressRepository;
        this.tokenService = tokenService;
    }

    public Set<AddressResponseDTO> findUserAddresses() {
        Optional<User> optionalUser = tokenService.getAuthUser();
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            return user.getActiveAddresses().stream()
                    .map(address -> new AddressResponseDTO(
                            address.getId(),
                            address.getZipCode(),
                            address.getNumber(),
                            address.getStreet(),
                            address.getNeighborhood(),
                            address.getCity(),
                            address.getState()
                    )).collect(Collectors.toSet());
        } else
            return null;
    }

    public void saveAddress(AddressRequestDTO data) {
        Optional<User> optionalUser = tokenService.getAuthUser();
        optionalUser.ifPresent(user -> {
            Address address = new Address(
                    data.zipCode(),
                    data.number(),
                    data.street(),
                    data.neighborhood(),
                    data.city(),
                    data.state(),
                    user);
            user.getActiveAddresses().add(address);
            addressRepository.save(address);
        });
    }

    public void updateAddress(AddressUpdateDTO data) {
        Optional<User> optionalUser = tokenService.getAuthUser();
        optionalUser.ifPresent(user -> {
            Optional<Address> optionalAddress = addressRepository.findById(data.id());
            if (optionalAddress.isPresent()){
                Address address = optionalAddress.get();
                if (user.getAddresses().contains(address)) {
                    address.setZipCode(data.zipCode());
                    address.setNumber(data.number());
                    address.setStreet(data.street());
                    address.setNeighborhood(data.neighborhood());
                    address.setCity(data.city());
                    address.setState(data.state());
                    addressRepository.save(address);
                } else
                    throw new RuntimeException("The address doesn't exist in your list!");
            } else
                throw new RuntimeException();
        });
    }

    public void deleteAddress(Long id) {
        Optional<User> optionalUser = tokenService.getAuthUser();
        optionalUser.ifPresent(user -> {
            Optional<Address> optionalAddress = addressRepository.findById(id);
            if(optionalAddress.isPresent() && optionalAddress.get().isActive()) {
                Address address = optionalAddress.get();
                Set<Address> addresses = user.getActiveAddresses();
                if(addresses.contains(optionalAddress.get())) {
                    if(!address.getPurchases().isEmpty()) {
                        address.setActive(false);
                        addressRepository.save(address);
                    } else
                        addressRepository.delete(address);
                } else
                    throw new RuntimeException("The address doesn't exist in your list!");
            } else
                throw new RuntimeException();
        });
    }
}
