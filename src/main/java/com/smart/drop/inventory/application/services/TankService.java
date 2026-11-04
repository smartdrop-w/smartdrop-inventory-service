package com.smart.drop.inventory.application.services;

import com.smart.drop.inventory.domain.model.entities.Tank;
import com.smart.drop.inventory.domain.model.repositories.TankRepository;
import com.smart.drop.inventory.interfaces.rest.dto.TankStatusResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TankService {

    private final TankRepository tankRepository;

    public TankService(TankRepository tankRepository) {
        this.tankRepository = tankRepository;
    }

    public Tank createTank(Integer userId, String name, Integer capacity, Integer current) {
        Tank tank = Tank.create(userId, name, capacity, current);
        return tankRepository.save(tank);
    }

    @Transactional(readOnly = true)
    public Optional<Tank> getById(Integer tankId) {
        return tankRepository.findById(tankId);
    }

    @Transactional(readOnly = true)
    public List<Tank> getByUserId(Integer userId) {
        return tankRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Tank> getAll() {
        return tankRepository.findAll();
    }

    public Tank updateTank(Integer tankId, String name, Integer capacity, Integer current) {
        Tank existing = tankRepository.findById(tankId)
                .orElseThrow(() -> new IllegalArgumentException("Tank not found: " + tankId));

        Tank updated = new Tank(
                existing.tankId(),
                existing.userId(),
                name,
                capacity,
                current
        );
        return tankRepository.save(updated);
    }

    public Tank replenish(Integer tankId) {
        Tank existing = tankRepository.findById(tankId)
                .orElseThrow(() -> new IllegalArgumentException("Tank not found: " + tankId));

        Tank replenished = new Tank(
                existing.tankId(),
                existing.userId(),
                existing.name(),
                existing.capacity(),
                existing.capacity()
        );
        return tankRepository.save(replenished);
    }

    @Transactional(readOnly = true)
    public TankStatusResponse getTankStatus(Integer tankId) {
        Tank tank = tankRepository.findById(tankId)
                .orElseThrow(() -> new IllegalArgumentException("Tank not found: " + tankId));

        double percentage = 0.0;
        if (tank.capacity() != null && tank.capacity() > 0 && tank.current() != null) {
            percentage = Math.round(((double) tank.current() / (double) tank.capacity()) * 1000.0) / 10.0;
        }

        String status;
        boolean isCritical = percentage < 15.0;
        boolean isLow = percentage < 30.0;

        if (isCritical) {
            status = "CRITICAL";
        } else if (isLow) {
            status = "LOW";
        } else if (percentage >= 95.0) {
            status = "FULL";
        } else {
            status = "NORMAL";
        }

        return new TankStatusResponse(
                tank.tankId(),
                tank.name(),
                tank.capacity(),
                tank.current(),
                percentage,
                status,
                isLow,
                isCritical,
                LocalDateTime.now()
        );
    }

    public void deleteById(Integer tankId) {
        tankRepository.deleteById(tankId);
    }
}
