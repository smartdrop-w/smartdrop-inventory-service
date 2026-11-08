package com.smart.drop.inventory.interfaces.rest;

import com.smart.drop.inventory.application.services.IrrigationConfigService;
import com.smart.drop.inventory.domain.model.entities.IrrigationConfig;
import com.smart.drop.inventory.interfaces.rest.dto.IrrigationConfigResponse;
import com.smart.drop.inventory.interfaces.rest.dto.UpdateIrrigationConfigRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para configuración de programas de riego automatizado y lecturas de humedad de suelo.
 */
@Tag(name = "Smart Irrigation Configuration", description = "Endpoints para configuración de ciclos de riego, humedad de suelo y anulación manual")
@RestController
@RequestMapping("/api/v1/inventory/irrigation")
public class IrrigationConfigController {

    private final IrrigationConfigService irrigationConfigService;

    public IrrigationConfigController(IrrigationConfigService irrigationConfigService) {
        this.irrigationConfigService = irrigationConfigService;
    }

    @Operation(summary = "Obtener configuración de riego por usuario", description = "Recupera la configuración de riego activa o la crea por defecto para el usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Configuración obtenida exitosamente", content = @Content(schema = @Schema(implementation = IrrigationConfigResponse.class)))
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<IrrigationConfigResponse> getByUserId(@PathVariable Integer userId) {
        IrrigationConfig config = irrigationConfigService.getOrCreateByUserId(userId);
        return ResponseEntity.ok(toResponse(config));
    }

    @Operation(summary = "Actualizar configuración de riego", description = "Modifica los parámetros de control manual, humedad de suelo o próximo turno de riego.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Configuración actualizada exitosamente", content = @Content(schema = @Schema(implementation = IrrigationConfigResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de configuración inválidos")
    })
    @PutMapping("/user/{userId}")
    public ResponseEntity<IrrigationConfigResponse> updateByUserId(
            @PathVariable Integer userId,
            @Valid @RequestBody UpdateIrrigationConfigRequest request
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
