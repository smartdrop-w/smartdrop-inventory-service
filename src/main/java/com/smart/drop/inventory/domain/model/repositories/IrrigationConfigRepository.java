package com.smart.drop.inventory.domain.model.repositories;

import com.smart.drop.inventory.domain.model.entities.IrrigationConfig;

import java.util.Optional;

/**
 * Domain repository contract for irrigation configs.
 */
public interface IrrigationConfigRepository {

    IrrigationConfig save(IrrigationConfig irrigationConfig);

    Optional<IrrigationConfig> findByUserId(Integer userId);
}
