package com.nemo.mixsafe.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredientId")
    private Long ingredientId;

    @Column(name = "casNo", nullable = false)
    private String casNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false)
    private Product product;
}
