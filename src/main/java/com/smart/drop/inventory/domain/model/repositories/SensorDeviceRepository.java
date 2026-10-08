package com.smart.drop.inventory.domain.model.repositories;

import com.smart.drop.inventory.domain.model.entities.SensorDevice;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository contract for sensor devices.
 */
public interface SensorDeviceRepository {

    SensorDevice save(SensorDevice sensorDevice);

    Optional<SensorDevice> findById(Integer deviceId);

    List<SensorDevice> findByUserId(Integer userId);

    List<SensorDevice> findAll();

    void deleteById(Integer deviceId);
}
