package com.smart.drop.inventory.infrastructure.persistence.jpa.repositories;

import com.smart.drop.inventory.infrastructure.persistence.jpa.entities.TankEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TankJpaRepository extends JpaRepository<TankEntity, Integer> {

    List<TankEntity> findByUserId(Integer userId);
}
