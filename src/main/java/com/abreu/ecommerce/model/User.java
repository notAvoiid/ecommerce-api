package com.abreu.ecommerce.model;

import com.abreu.ecommerce.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true, length = 11)
    private String CPF;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Number number;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Address> addresses;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Order> cart;
    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<Product> wishlist;
    @OneToMany(mappedBy = "user")
    private List<Purchase> purchases;

    public User(String name, String username, String CPF, String email, String password) {
        this.name = name;
        this.username = username;
        this.CPF = CPF;
        this.email = email;
        this.password = password;
        this.role = UserRole.USER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public Set<Address> getActiveAddresses() {
        return addresses.stream()
                .filter(Address::isActive)
                .collect(Collectors.toSet());
    }

    public List<Order> getActiveCart() {
        return cart.stream()
                .filter(order -> !order.isCompleted())
                .collect(Collectors.toList());
    }
}