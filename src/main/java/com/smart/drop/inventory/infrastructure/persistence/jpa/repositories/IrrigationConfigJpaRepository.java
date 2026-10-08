package com.smart.drop.inventory.infrastructure.persistence.jpa.repositories;

import com.smart.drop.inventory.infrastructure.persistence.jpa.entities.IrrigationConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IrrigationConfigJpaRepository extends JpaRepository<IrrigationConfigEntity, Integer> {

    Optional<IrrigationConfigEntity> findByUserId(Integer userId);
}
