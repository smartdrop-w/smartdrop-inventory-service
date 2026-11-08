package com.smart.drop.inventory.interfaces.rest;

import com.smart.drop.inventory.application.services.ConsumptionService;
import com.smart.drop.inventory.domain.model.entities.Consumption;
import com.smart.drop.inventory.interfaces.rest.dto.ConsumptionResponse;
import com.smart.drop.inventory.interfaces.rest.dto.RegisterConsumptionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para registro y consulta de consumo hídrico de usuarios.
 */
@Tag(name = "Water Consumption", description = "Endpoints para registro y trazabilidad histórica de consumos volumétricos")
@RestController
@RequestMapping("/api/v1/inventory/consumptions")
public class ConsumptionController {

    private final ConsumptionService consumptionService;

    public ConsumptionController(ConsumptionService consumptionService) {
        this.consumptionService = consumptionService;
    }

    @Operation(summary = "Registrar consumo de agua", description = "Registra un consumo volumétrico en litros y actualiza el balance de inventario correspondiente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Consumo registrado con éxito", content = @Content(schema = @Schema(implementation = ConsumptionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros de volumen o usuario inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<ConsumptionResponse> register(@Valid @RequestBody RegisterConsumptionRequest request) {
        Consumption created = consumptionService.registerConsumption(
                request.userId(),
                request.volumeUsed(),
                request.usageContext()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @Operation(summary = "Obtener consumo por ID", description = "Recupera la transacción de consumo por su identificador numérico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consumo encontrado", content = @Content(schema = @Schema(implementation = ConsumptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Consumo no encontrado")
    })
    @GetMapping("/{consumptionId}")
    public ResponseEntity<ConsumptionResponse> getById(@PathVariable Integer consumptionId) {
        return consumptionService.getById(consumptionId)
                .map(consumption -> ResponseEntity.ok(toResponse(consumption)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar consumos por usuario", description = "Retorna el historial de consumos hídricos de un cliente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historial de consumo recuperado")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ConsumptionResponse>> getByUserId(@PathVariable Integer userId) {
        List<ConsumptionResponse> data = consumptionService.getByUserId(userId).stream().map(this::toResponse).toList();
        return ResponseEntity.ok(data);
    }

    @Operation(summary = "Listar todos los consumos", description = "Retorna la colección completa de transacciones de consumo registradas en la plataforma.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista completa de consumos")
    })
    @GetMapping
    public ResponseEntity<List<ConsumptionResponse>> getAll() {
        List<ConsumptionResponse> data = consumptionService.getAll().stream().map(this::toResponse).toList();
        return ResponseEntity.ok(data);
    }

    @Operation(summary = "Eliminar registro de consumo", description = "Remueve un registro de consumo del sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Consumo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Consumo no encontrado")
    })
    @DeleteMapping("/{consumptionId}")
    public ResponseEntity<Void> delete(@PathVariable Integer consumptionId) {
        consumptionService.deleteById(consumptionId);
        return ResponseEntity.noContent().build();
    }

    private ConsumptionResponse toResponse(Consumption consumption) {
        return new ConsumptionResponse(
                consumption.consumptionId(),
                consumption.userId(),
                consumption.volumeUsed(),
                consumption.usageContext(),
                consumption.dateRecorded()
        );
    }
}
