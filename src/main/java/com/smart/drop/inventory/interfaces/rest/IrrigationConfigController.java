package com.smart.drop.inventory.interfaces.rest;

import com.smart.drop.inventory.application.services.IrrigationConfigService;
import com.smart.drop.inventory.domain.model.entities.IrrigationConfig;
import com.smart.drop.inventory.interfaces.rest.dto.IrrigationConfigResponse;
import com.smart.drop.inventory.interfaces.rest.dto.UpdateIrrigationConfigRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory/irrigation")
public class IrrigationConfigController {

    private final IrrigationConfigService irrigationConfigService;

    public IrrigationConfigController(IrrigationConfigService irrigationConfigService) {
        this.irrigationConfigService = irrigationConfigService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<IrrigationConfigResponse> getByUserId(@PathVariable Integer userId) {
        IrrigationConfig config = irrigationConfigService.getOrCreateByUserId(userId);
        return ResponseEntity.ok(toResponse(config));
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<IrrigationConfigResponse> updateByUserId(
            @PathVariable Integer userId,
            @RequestBody UpdateIrrigationConfigRequest request
    ) {
        IrrigationConfig updated = irrigationConfigService.saveOrUpdate(
                userId,
                request.manualOverride(),
                request.soilMoisture(),
                request.irrigationNext()
        );
        return ResponseEntity.ok(toResponse(updated));
    }

    private IrrigationConfigResponse toResponse(IrrigationConfig config) {
        return new IrrigationConfigResponse(
                config.irrigationConfigId(),
                config.userId(),
                config.manualOverride(),
                config.soilMoisture(),
                config.irrigationNext()
        );
    }
}
