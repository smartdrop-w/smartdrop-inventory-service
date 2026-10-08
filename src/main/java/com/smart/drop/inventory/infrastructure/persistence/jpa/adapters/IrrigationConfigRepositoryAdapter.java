package com.smart.drop.inventory.infrastructure.persistence.jpa.adapters;

import com.smart.drop.inventory.domain.model.entities.IrrigationConfig;
import com.smart.drop.inventory.domain.model.repositories.IrrigationConfigRepository;
import com.smart.drop.inventory.infrastructure.persistence.jpa.entities.IrrigationConfigEntity;
import com.smart.drop.inventory.infrastructure.persistence.jpa.repositories.IrrigationConfigJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class IrrigationConfigRepositoryAdapter implements IrrigationConfigRepository {

    private final IrrigationConfigJpaRepository irrigationConfigJpaRepository;

    public IrrigationConfigRepositoryAdapter(IrrigationConfigJpaRepository irrigationConfigJpaRepository) {
        this.irrigationConfigJpaRepository = irrigationConfigJpaRepository;
    }

    @Override
    public IrrigationConfig save(IrrigationConfig irrigationConfig) {
        IrrigationConfigEntity entity = toEntity(irrigationConfig);
        IrrigationConfigEntity saved = irrigationConfigJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<IrrigationConfig> findByUserId(Integer userId) {
        return irrigationConfigJpaRepository.findByUserId(userId).map(this::toDomain);
    }

    private IrrigationConfigEntity toEntity(IrrigationConfig config) {
        IrrigationConfigEntity entity = new IrrigationConfigEntity();
        entity.setIrrigationConfigId(config.irrigationConfigId());
        entity.setUserId(config.userId());
        entity.setManualOverride(config.manualOverride());
        entity.setSoilMoisture(config.soilMoisture());
        entity.setIrrigationNext(config.irrigationNext());
        return entity;
    }

    private IrrigationConfig toDomain(IrrigationConfigEntity entity) {
        return new IrrigationConfig(
                entity.getIrrigationConfigId(),
                entity.getUserId(),
                entity.getManualOverride(),
                entity.getSoilMoisture(),
                entity.getIrrigationNext()
        );
    }
}
