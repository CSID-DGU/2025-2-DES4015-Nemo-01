package com.nemo.mixsafe.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = true)
    private String prdtMstrNo;

    @Column(nullable = true)
    private String prdtarmCd;

    @Column(nullable = false)
    private Boolean isDanger;
}
