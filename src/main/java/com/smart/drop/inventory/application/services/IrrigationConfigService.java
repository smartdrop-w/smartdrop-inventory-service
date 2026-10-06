package com.smart.drop.inventory.application.services;

import com.smart.drop.inventory.domain.model.entities.IrrigationConfig;
import com.smart.drop.inventory.domain.model.repositories.IrrigationConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IrrigationConfigService {

    private final IrrigationConfigRepository irrigationConfigRepository;

    public IrrigationConfigService(IrrigationConfigRepository irrigationConfigRepository) {
        this.irrigationConfigRepository = irrigationConfigRepository;
    }

    @Transactional(readOnly = true)
    public IrrigationConfig getOrCreateByUserId(Integer userId) {
        return irrigationConfigRepository.findByUserId(userId)
                .orElseGet(() -> IrrigationConfig.create(userId, false, 0, "Not scheduled"));
    }

    public IrrigationConfig saveOrUpdate(Integer userId, Boolean manualOverride, Integer soilMoisture, String irrigationNext) {
        IrrigationConfig existing = irrigationConfigRepository.findByUserId(userId).orElse(null);

        IrrigationConfig config;
        if (existing == null) {
            config = IrrigationConfig.create(userId, manualOverride, soilMoisture, irrigationNext);
        } else {
            config = new IrrigationConfig(
                    existing.irrigationConfigId(),
                    existing.userId(),
                    manualOverride != null ? manualOverride : existing.manualOverride(),
                    soilMoisture != null ? soilMoisture : existing.soilMoisture(),
                    irrigationNext != null ? irrigationNext : existing.irrigationNext()
            );
        }
        return irrigationConfigRepository.save(config);
    }
}
