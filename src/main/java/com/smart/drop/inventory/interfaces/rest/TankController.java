package com.smart.drop.inventory.interfaces.rest;

import com.smart.drop.inventory.application.services.TankService;
import com.smart.drop.inventory.domain.model.entities.Tank;
import com.smart.drop.inventory.interfaces.rest.dto.CreateTankRequest;
import com.smart.drop.inventory.interfaces.rest.dto.TankResponse;
import com.smart.drop.inventory.interfaces.rest.dto.TankStatusResponse;
import com.smart.drop.inventory.interfaces.rest.dto.UpdateTankRequest;
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
 * Controlador REST para administración de tanques de agua y reservorios.
 */
@Tag(name = "Tanks Management", description = "Endpoints para registro, monitoreo de nivel, reabastecimiento y evaluación de estado de tanques de agua")
@RestController
@RequestMapping("/api/v1/inventory/tanks")
public class TankController {

    private final TankService tankService;

    public TankController(TankService tankService) {
        this.tankService = tankService;
    }

    @Operation(summary = "Registrar nuevo tanque", description = "Crea un nuevo tanque de almacenamiento indicando capacidad total y nivel inicial.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tanque creado exitosamente", content = @Content(schema = @Schema(implementation = TankResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros de capacidad o usuario inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<TankResponse> create(@Valid @RequestBody CreateTankRequest request) {
        Tank created = tankService.createTank(
                request.userId(),
                request.name(),
                request.capacity(),
                request.current()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @Operation(summary = "Obtener tanque por ID", description = "Recupera los datos de un tanque por su identificador numérico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tanque encontrado", content = @Content(schema = @Schema(implementation = TankResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tanque no encontrado")
    })
    @GetMapping("/{tankId}")
    public ResponseEntity<TankResponse> getById(@PathVariable Integer tankId) {
        return tankService.getById(tankId)
                .map(tank -> ResponseEntity.ok(toResponse(tank)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Consultar estado operacional y alertas del tanque", description = "Calcula en tiempo real el porcentaje de llenado del tanque y emite banderas de alerta (LOW, CRITICAL).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado del tanque calculado exitosamente", content = @Content(schema = @Schema(implementation = TankStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tanque no encontrado")
    })
    @GetMapping("/{tankId}/status")
    public ResponseEntity<TankStatusResponse> getTankStatus(@PathVariable Integer tankId) {
        TankStatusResponse status = tankService.getTankStatus(tankId);
        return ResponseEntity.ok(status);
    }

    @Operation(summary = "Listar tanques por usuario", description = "Recupera todos los tanques registrados que pertenecen a un usuario específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Colección de tanques recuperada exitosamente")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TankResponse>> getByUserId(@PathVariable Integer userId) {
        List<TankResponse> data = tankService.getByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(data);
    }

    @Operation(summary = "Listar todos los tanques", description = "Retorna el inventario completo de tanques monitoreados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista completa de tanques")
    })
    @GetMapping
    public ResponseEntity<List<TankResponse>> getAll() {
        List<TankResponse> data = tankService.getAll()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(data);
    }

    @Operation(summary = "Actualizar tanque", description = "Modifica el nombre, capacidad o nivel actual de un tanque existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tanque actualizado exitosamente", content = @Content(schema = @Schema(implementation = TankResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tanque no encontrado")
    })
    @PutMapping("/{tankId}")
    public ResponseEntity<TankResponse> update(
            @PathVariable Integer tankId,
            @Valid @RequestBody UpdateTankRequest request
    ) {
        Tank updated = tankService.updateTank(
                tankId,
                request.name(),
                request.capacity(),
                request.current()
        );
        return ResponseEntity.ok(toResponse(updated));
    }

    @Operation(summary = "Reabastecer tanque al 100%", description = "Restaura el nivel actual del tanque al total de su capacidad nominal tras un suministro de agua.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tanque reabastecido a máxima capacidad", content = @Content(schema = @Schema(implementation = TankResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tanque no encontrado")
    })
    @PutMapping("/{tankId}/replenish")
    public ResponseEntity<TankResponse> replenish(@PathVariable Integer tankId) {
        Tank updated = tankService.replenish(tankId);
        return ResponseEntity.ok(toResponse(updated));
    }

    @Operation(summary = "Eliminar tanque", description = "Remueve un tanque del registro del sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tanque eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tanque no encontrado")
    })
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
