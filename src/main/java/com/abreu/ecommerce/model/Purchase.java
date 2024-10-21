package com.abreu.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "tb_purchases")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double totalPrice;

    @OneToMany(mappedBy = "purchase")
    @Column(nullable = false)
    private Set<Order> orders;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Purchase(Set<Order> orders, Double totalPrice, Address address, User user) {
        this.orders = orders;
        this.address = address;
        this.user = user;
        this.totalPrice = totalPrice;
    }
}
