package com.smart.drop.inventory.infrastructure.persistence.proxy;

import com.smart.drop.inventory.infrastructure.persistence.jpa.entities.TankEntity;
import com.smart.drop.inventory.infrastructure.persistence.jpa.repositories.TankJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * RealSubject del Patrón Proxy.
 * Ejecuta las consultas directamente contra la base de datos relacional mediante Spring Data JPA.
 */
@Service("databaseTankReadService")
public class DatabaseTankReadService implements TankReadService {

    private static final Logger log = LoggerFactory.getLogger(DatabaseTankReadService.class);

    private final TankJpaRepository tankRepository;

    public DatabaseTankReadService(TankJpaRepository tankRepository) {
        this.tankRepository = tankRepository;
    }

    @Override
    public Optional<TankEntity> findTankById(Integer tankId) {
        log.debug("[SQL Query] Ejecutando SELECT a la base de datos para tankId={}", tankId);
        return tankRepository.findById(tankId);
    }

    @Override
    public List<TankEntity> findTanksByUserId(Integer userId) {
        log.debug("[SQL Query] Ejecutando SELECT a la base de datos para userId={}", userId);
        return tankRepository.findByUserId(userId);
    }

    @Override
    public void evictCache(Integer tankId) {
        // No-op en la implementación directa de base de datos
    }
}
