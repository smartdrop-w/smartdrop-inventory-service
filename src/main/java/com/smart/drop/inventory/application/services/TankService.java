package com.smart.drop.inventory.application.services;

import com.smart.drop.inventory.domain.model.entities.Tank;
import com.smart.drop.inventory.domain.model.repositories.TankRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void deleteById(Integer tankId) {
        tankRepository.deleteById(tankId);
    }
}
