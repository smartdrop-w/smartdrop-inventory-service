package com.smart.drop.inventory.infrastructure.persistence.jpa.adapters;

import com.smart.drop.inventory.domain.model.entities.Consumption;
import com.smart.drop.inventory.domain.model.repositories.ConsumptionRepository;
import com.smart.drop.inventory.infrastructure.persistence.jpa.entities.ConsumptionEntity;
import com.smart.drop.inventory.infrastructure.persistence.jpa.repositories.ConsumptionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ConsumptionRepositoryAdapter implements ConsumptionRepository {

    private final ConsumptionJpaRepository consumptionJpaRepository;

    public ConsumptionRepositoryAdapter(ConsumptionJpaRepository consumptionJpaRepository) {
        this.consumptionJpaRepository = consumptionJpaRepository;
    }

    @Override
    public Consumption save(Consumption consumption) {
        ConsumptionEntity entity = toEntity(consumption);
        ConsumptionEntity saved = consumptionJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Consumption> findById(Integer consumptionId) {
        return consumptionJpaRepository.findById(consumptionId).map(this::toDomain);
    }

    @Override
    public List<Consumption> findByUserId(Integer userId) {
        return consumptionJpaRepository.findByUserId(userId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Consumption> findAll() {
        return consumptionJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public void deleteById(Integer consumptionId) {
        consumptionJpaRepository.deleteById(consumptionId);
    }

    private ConsumptionEntity toEntity(Consumption consumption) {
        ConsumptionEntity entity = new ConsumptionEntity();
        entity.setConsumptionId(consumption.consumptionId());
        entity.setUserId(consumption.userId());
        entity.setVolumeUsed(consumption.volumeUsed());
        entity.setDateRecorded(consumption.dateRecorded());
        entity.setUsageContext(consumption.usageContext());
        return entity;
    }

    private Consumption toDomain(ConsumptionEntity entity) {
        return new Consumption(
                entity.getConsumptionId(),
                entity.getUserId(),
                entity.getVolumeUsed(),
                entity.getDateRecorded(),
                entity.getUsageContext()
        );
    }
}

