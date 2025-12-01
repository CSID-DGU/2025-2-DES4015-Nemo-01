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
    @JoinColumn(name = "productId")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "substanceId")
    private DefaultSubstance substance;

    @PrePersist
    @PreUpdate
    private void validateTarget() {
        if (product == null && substance == null) {
            throw new IllegalArgumentException("Ingredient는 product 또는 substance 중 하나는 반드시 있어야 합니다.");
        }
    }
}
