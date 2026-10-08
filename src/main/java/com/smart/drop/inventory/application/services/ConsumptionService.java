package com.smart.drop.inventory.application.services;

import com.smart.drop.inventory.domain.model.entities.Consumption;
import com.smart.drop.inventory.domain.model.entities.Inventory;
import com.smart.drop.inventory.domain.model.repositories.ConsumptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ConsumptionService {

    private final ConsumptionRepository consumptionRepository;
    private final InventoryService inventoryService;

    public ConsumptionService(ConsumptionRepository consumptionRepository,
                              InventoryService inventoryService) {
        this.consumptionRepository = consumptionRepository;
        this.inventoryService = inventoryService;
    }

    public Consumption registerConsumption(Integer userId, BigDecimal volumeUsed, String usageContext) {
        Consumption consumption = Consumption.create(userId, volumeUsed, usageContext);
        Consumption savedConsumption = consumptionRepository.save(consumption);

        // Keeps inventory and consumption consistent in one application use case.
        inventoryService.subtractConsumption(userId, volumeUsed);

        return savedConsumption;
    }

    @Transactional(readOnly = true)
    public Optional<Consumption> getById(Integer consumptionId) {
        return consumptionRepository.findById(consumptionId);
    }

    @Transactional(readOnly = true)
    public List<Consumption> getByUserId(Integer userId) {
        return consumptionRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Consumption> getAll() {
        return consumptionRepository.findAll();
    }

    public void deleteById(Integer consumptionId) {
        consumptionRepository.deleteById(consumptionId);
    }
}

