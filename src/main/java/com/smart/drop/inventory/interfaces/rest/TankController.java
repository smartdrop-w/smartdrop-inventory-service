package com.smart.drop.inventory.interfaces.rest;

import com.smart.drop.inventory.application.services.TankService;
import com.smart.drop.inventory.domain.model.entities.Tank;
import com.smart.drop.inventory.interfaces.rest.dto.CreateTankRequest;
import com.smart.drop.inventory.interfaces.rest.dto.TankResponse;
import com.smart.drop.inventory.interfaces.rest.dto.UpdateTankRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory/tanks")
public class TankController {

    private final TankService tankService;

    public TankController(TankService tankService) {
        this.tankService = tankService;
    }

    @PostMapping
    public ResponseEntity<TankResponse> create(@RequestBody CreateTankRequest request) {
        Tank created = tankService.createTank(
                request.userId(),
                request.name(),
                request.capacity(),
                request.current()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @GetMapping("/{tankId}")
    public ResponseEntity<TankResponse> getById(@PathVariable Integer tankId) {
        return tankService.getById(tankId)
                .map(tank -> ResponseEntity.ok(toResponse(tank)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TankResponse>> getByUserId(@PathVariable Integer userId) {
        List<TankResponse> data = tankService.getByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(data);
    }

    @GetMapping
    public ResponseEntity<List<TankResponse>> getAll() {
        List<TankResponse> data = tankService.getAll()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(data);
    }

    @PutMapping("/{tankId}")
    public ResponseEntity<TankResponse> update(
            @PathVariable Integer tankId,
            @RequestBody UpdateTankRequest request
    ) {
        Tank updated = tankService.updateTank(
                tankId,
                request.name(),
                request.capacity(),
                request.current()
        );
        return ResponseEntity.ok(toResponse(updated));
    }

    @PutMapping("/{tankId}/replenish")
    public ResponseEntity<TankResponse> replenish(@PathVariable Integer tankId) {
        Tank updated = tankService.replenish(tankId);
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{tankId}")
    public ResponseEntity<Void> delete(@PathVariable Integer tankId) {
        tankService.deleteById(tankId);
        return ResponseEntity.noContent().build();
    }

    private TankResponse toResponse(Tank tank) {
        return new TankResponse(
                tank.tankId(),
                tank.userId(),
                tank.name(),
                tank.capacity(),
                tank.current()
        );
    }
}
