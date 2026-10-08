package com.smart.drop.inventory.interfaces.rest;

import com.smart.drop.inventory.application.services.SensorDeviceService;
import com.smart.drop.inventory.domain.model.entities.SensorDevice;
import com.smart.drop.inventory.interfaces.rest.dto.CreateSensorDeviceRequest;
import com.smart.drop.inventory.interfaces.rest.dto.SensorDeviceResponse;
import com.smart.drop.inventory.interfaces.rest.dto.UpdateSensorDeviceRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory/devices")
public class SensorDeviceController {

    private final SensorDeviceService sensorDeviceService;

    public SensorDeviceController(SensorDeviceService sensorDeviceService) {
        this.sensorDeviceService = sensorDeviceService;
    }

    @PostMapping
    public ResponseEntity<SensorDeviceResponse> create(@RequestBody CreateSensorDeviceRequest request) {
        SensorDevice created = sensorDeviceService.createDevice(
                request.userId(),
                request.name(),
                request.location(),
                request.flow(),
                request.daily(),
                request.battery(),
                request.status()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<SensorDeviceResponse> getById(@PathVariable Integer deviceId) {
        return sensorDeviceService.getById(deviceId)
                .map(device -> ResponseEntity.ok(toResponse(device)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SensorDeviceResponse>> getByUserId(@PathVariable Integer userId) {
        List<SensorDeviceResponse> data = sensorDeviceService.getByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(data);
    }

    @GetMapping
    public ResponseEntity<List<SensorDeviceResponse>> getAll() {
        List<SensorDeviceResponse> data = sensorDeviceService.getAll()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(data);
    }

    @PutMapping("/{deviceId}")
    public ResponseEntity<SensorDeviceResponse> update(
            @PathVariable Integer deviceId,
            @RequestBody UpdateSensorDeviceRequest request
    ) {
        SensorDevice updated = sensorDeviceService.updateDevice(
                deviceId,
                request.name(),
                request.location(),
                request.flow(),
                request.daily(),
                request.battery(),
                request.status()
        );
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Void> delete(@PathVariable Integer deviceId) {
        sensorDeviceService.deleteById(deviceId);
        return ResponseEntity.noContent().build();
    }

    private SensorDeviceResponse toResponse(SensorDevice device) {
        return new SensorDeviceResponse(
                device.deviceId(),
                device.userId(),
                device.name(),
                device.location(),
                device.flow(),
                device.daily(),
                device.battery(),
                device.status()
        );
    }
}
