package com.smart.drop.inventory.infrastructure.persistence.jpa.repositories;

import com.smart.drop.inventory.infrastructure.persistence.jpa.entities.SensorDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorDeviceJpaRepository extends JpaRepository<SensorDeviceEntity, Integer> {

    List<SensorDeviceEntity> findByUserId(Integer userId);
}
