package com.smart.drop.inventory.infrastructure.persistence.jpa.repositories;

import com.smart.drop.inventory.infrastructure.persistence.jpa.entities.ConsumptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsumptionJpaRepository extends JpaRepository<ConsumptionEntity, Integer> {

    List<ConsumptionEntity> findByUserId(Integer userId);
}

