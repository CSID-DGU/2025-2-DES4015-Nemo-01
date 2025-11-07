package com.nemo.mixsafe.repository;

import com.nemo.mixsafe.domain.Ingredient;
import com.nemo.mixsafe.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    boolean existsByProductAndCasNo(Product product, String casNo);

    Ingredient save(Ingredient ingredient);
}