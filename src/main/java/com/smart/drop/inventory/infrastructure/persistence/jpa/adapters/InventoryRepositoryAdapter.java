package com.smart.drop.inventory.infrastructure.persistence.jpa.adapters;

import com.smart.drop.inventory.domain.model.entities.Inventory;
import com.smart.drop.inventory.domain.model.repositories.InventoryRepository;
import com.smart.drop.inventory.infrastructure.persistence.jpa.entities.InventoryEntity;
import com.smart.drop.inventory.infrastructure.persistence.jpa.repositories.InventoryJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class InventoryRepositoryAdapter implements InventoryRepository {

    private final InventoryJpaRepository inventoryJpaRepository;

    public InventoryRepositoryAdapter(InventoryJpaRepository inventoryJpaRepository) {
        this.inventoryJpaRepository = inventoryJpaRepository;
    }

    @Override
    public Inventory save(Inventory inventory) {
        InventoryEntity entity = toEntity(inventory);
        InventoryEntity saved = inventoryJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Inventory update(Inventory inventory) {
        InventoryEntity existing = inventoryJpaRepository.findById(inventory.inventoryId())
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found: " + inventory.inventoryId()));

        existing.setUserId(inventory.userId());
        existing.setAvailableLiters(inventory.availableLiters());
        existing.setLastRefillDate(inventory.lastRefillDate());
        existing.setNextRefillPrediction(inventory.nextRefillPrediction());

        InventoryEntity updated = inventoryJpaRepository.save(existing);
        return toDomain(updated);
    }

    @Override
    public Optional<Inventory> findById(Integer inventoryId) {
        return inventoryJpaRepository.findById(inventoryId).map(this::toDomain);
    }

    @Override
    public Optional<Inventory> findByUserId(Integer userId) {
        return inventoryJpaRepository.findByUserId(userId).map(this::toDomain);
    }

    @Override
    public List<Inventory> findAll() {
        return inventoryJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(Integer inventoryId) {
        inventoryJpaRepository.deleteById(inventoryId);
    }

    private InventoryEntity toEntity(Inventory inventory) {
        InventoryEntity entity = new InventoryEntity();
        entity.setInventoryId(inventory.inventoryId());
        entity.setUserId(inventory.userId());
        entity.setAvailableLiters(inventory.availableLiters());
        entity.setLastRefillDate(inventory.lastRefillDate());
        entity.setNextRefillPrediction(inventory.nextRefillPrediction());
        return entity;
    }

    private Inventory toDomain(InventoryEntity entity) {
        return new Inventory(
                entity.getInventoryId(),
                entity.getUserId(),
                entity.getAvailableLiters(),
                entity.getLastRefillDate(),
                entity.getNextRefillPrediction()
        );
    }
}

