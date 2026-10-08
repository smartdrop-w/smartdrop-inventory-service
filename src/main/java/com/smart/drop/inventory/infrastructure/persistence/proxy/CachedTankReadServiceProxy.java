package com.smart.drop.inventory.infrastructure.persistence.proxy;

import com.smart.drop.inventory.infrastructure.persistence.jpa.entities.TankEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Proxy (GoF) con patrón Cache-Aside para el acceso optimizado a los datos de los tanques.
 * Intercepta las llamadas de consulta:
 * - Ante un Cache-Hit: responde inmediatamente en memoria sin saturar la base de datos (< 1ms).
 * - Ante un Cache-Miss: delega al DatabaseTankReadService real y puebla la memoria local.
 * - Soporta métricas de aciertos/fallos y desalojo proactivo (Cache Invalidation).
 */
@Service
@Primary
public class CachedTankReadServiceProxy implements TankReadService {

    private static final Logger log = LoggerFactory.getLogger(CachedTankReadServiceProxy.class);

    private final TankReadService realService;
    private final Map<Integer, TankEntity> tankCache = new ConcurrentHashMap<>();
    private final Map<Integer, List<TankEntity>> userTanksCache = new ConcurrentHashMap<>();

    private final AtomicLong hitCount = new AtomicLong(0);
    private final AtomicLong missCount = new AtomicLong(0);

    public CachedTankReadServiceProxy(@Qualifier("databaseTankReadService") TankReadService realService) {
        this.realService = realService;
    }

    @Override
    public Optional<TankEntity> findTankById(Integer tankId) {
        if (tankId == null) {
            return Optional.empty();
        }

        if (tankCache.containsKey(tankId)) {
            hitCount.incrementAndGet();
            log.debug("[Proxy Cache HIT] Obtenido tanque {} desde memoria en <1ms", tankId);
            return Optional.of(tankCache.get(tankId));
        }

        missCount.incrementAndGet();
        log.debug("[Proxy Cache MISS] Tanque {} no encontrado en caché, delegando al servicio real", tankId);
        Optional<TankEntity> result = realService.findTankById(tankId);
        result.ifPresent(tank -> tankCache.put(tankId, tank));
        return result;
    }

    @Override
    public List<TankEntity> findTanksByUserId(Integer userId) {
        if (userId == null) {
            return List.of();
        }

        if (userTanksCache.containsKey(userId)) {
            hitCount.incrementAndGet();
            log.debug("[Proxy Cache HIT] Obtenida lista de tanques para usuario {} desde caché", userId);
            return userTanksCache.get(userId);
        }

        missCount.incrementAndGet();
        List<TankEntity> result = realService.findTanksByUserId(userId);
        if (!result.isEmpty()) {
            userTanksCache.put(userId, result);
        }
        return result;
    }

    @Override
    public void evictCache(Integer tankId) {
        if (tankId != null) {
            TankEntity removed = tankCache.remove(tankId);
            if (removed != null && removed.getUserId() != null) {
                userTanksCache.remove(removed.getUserId());
            }
            log.info("[Proxy Cache EVICT] Desalojada caché para tanque {}", tankId);
        }
    }

    public void clearAllCache() {
        tankCache.clear();
        userTanksCache.clear();
        log.info("[Proxy Cache CLEAR] Se limpió la totalidad de la caché en memoria");
    }

    public long getHitCount() {
        return hitCount.get();
    }

    public long getMissCount() {
        return missCount.get();
    }

    public int getCachedTanksCount() {
        return tankCache.size();
    }
}
