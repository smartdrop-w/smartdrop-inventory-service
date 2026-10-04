package com.smart.drop.inventory.infrastructure.persistence.proxy;

import com.smart.drop.inventory.infrastructure.persistence.jpa.entities.TankEntity;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz común para el Patrón Proxy (GoF).
 * Define las operaciones de lectura y consulta sobre el estado de los tanques.
 */
public interface TankReadService {

    /**
     * Busca un tanque por su ID.
     */
    Optional<TankEntity> findTankById(Integer tankId);

    /**
     * Obtiene todos los tanques asignados a un usuario específico.
     */
    List<TankEntity> findTanksByUserId(Integer userId);

    /**
     * Invalida la caché para un tanque modificado (Cache Invalidation).
     */
    void evictCache(Integer tankId);
}
