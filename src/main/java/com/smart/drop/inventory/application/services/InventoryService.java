package com.smart.drop.inventory.application.services;

import com.smart.drop.inventory.domain.model.entities.Inventory;
import com.smart.drop.inventory.domain.model.repositories.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Inventory createInventory(Integer userId, BigDecimal availableLiters, LocalDate lastRefillDate, LocalDate nextRefillPrediction) {
        Inventory inventory = Inventory.create(userId, availableLiters, lastRefillDate, nextRefillPrediction);
        return inventoryRepository.save(inventory);
    }

    @Transactional(readOnly = true)
    public Optional<Inventory> getById(Integer inventoryId) {
        return inventoryRepository.findById(inventoryId);
    }

    @Transactional(readOnly = true)
    public Optional<Inventory> getByUserId(Integer userId) {
        return inventoryRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Inventory> getAll() {
        return inventoryRepository.findAll();
    }

    public Inventory updateInventory(Integer inventoryId, BigDecimal availableLiters, LocalDate lastRefillDate, LocalDate nextRefillPrediction) {
        Inventory current = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found: " + inventoryId));

        Inventory updated = new Inventory(
                current.inventoryId(),
                current.userId(),
                availableLiters,
                lastRefillDate,
                nextRefillPrediction
        );
        return inventoryRepository.update(updated);
    }

    public Inventory subtractConsumption(Integer userId, BigDecimal consumedVolume) {
        Inventory current = inventoryRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for user: " + userId));

        BigDecimal currentVolume = current.availableLiters() == null ? BigDecimal.ZERO : current.availableLiters();
        BigDecimal consumed = consumedVolume == null ? BigDecimal.ZERO : consumedVolume;
        BigDecimal updatedVolume = currentVolume.subtract(consumed);
        if (updatedVolume.signum() < 0) {
            throw new IllegalStateException("Not enough available liters for user: " + userId);
        }

        Inventory updated = current.withAvailableLiters(updatedVolume);
        return inventoryRepository.update(updated);
    }

    public void deleteById(Integer inventoryId) {
        inventoryRepository.deleteById(inventoryId);
    }
}

