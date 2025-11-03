package com.nemo.mixsafe.repository.Client;

import com.nemo.mixsafe.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SearchRepository extends JpaRepository<Product, Long> {
    Optional<Product> findFirstByProductName(String productName); //productName 기준 검색
}
