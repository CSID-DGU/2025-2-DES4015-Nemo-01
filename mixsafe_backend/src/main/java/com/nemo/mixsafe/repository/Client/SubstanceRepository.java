package com.nemo.mixsafe.repository.Client;

import com.nemo.mixsafe.domain.DefaultSubstance;
import com.nemo.mixsafe.repository.IngredientRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubstanceRepository extends JpaRepository<DefaultSubstance, Long> {
    Optional<DefaultSubstance> findBySubstanceName(String substanceName);
}
