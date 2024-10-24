package com.abreu.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_number")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Number {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String areaCode;
    @Column(nullable = false, unique = true)
    private String number;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Number(String areaCode, String number, User user) {
        this.areaCode = areaCode;
        this.number = number;
        this.user = user;
    }
}
