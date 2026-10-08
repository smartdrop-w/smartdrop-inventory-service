package com.smart.drop.inventory.domain.model.repositories;

import com.smart.drop.inventory.domain.model.entities.Consumption;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository contract for consumption records.
 */
public interface ConsumptionRepository {

    Consumption save(Consumption consumption);

    Optional<Consumption> findById(Integer consumptionId);

    List<Consumption> findByUserId(Integer userId);

    List<Consumption> findAll();

    void deleteById(Integer consumptionId);
}

