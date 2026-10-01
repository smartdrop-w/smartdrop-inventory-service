package com.smart.drop.inventory.domain.model.repositories;

import com.smart.drop.inventory.domain.model.entities.Inventory;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository contract for inventory records.
 */
public interface InventoryRepository {

    Inventory save(Inventory inventory);

    Inventory update(Inventory inventory);

    Optional<Inventory> findById(Integer inventoryId);

    Optional<Inventory> findByUserId(Integer userId);

    List<Inventory> findAll();

    void deleteById(Integer inventoryId);
}

