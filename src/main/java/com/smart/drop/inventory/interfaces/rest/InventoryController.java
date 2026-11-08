package com.smart.drop.inventory.interfaces.rest;

import com.smart.drop.inventory.application.services.InventoryService;
import com.smart.drop.inventory.domain.model.entities.Inventory;
import com.smart.drop.inventory.interfaces.rest.dto.CreateInventoryRequest;
import com.smart.drop.inventory.interfaces.rest.dto.InventoryResponse;
import com.smart.drop.inventory.interfaces.rest.dto.UpdateInventoryRequest;
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
 * Controlador REST para administración de inventarios hídricos y balances globales de recarga.
 */
@Tag(name = "Water Inventory Balance", description = "Endpoints para control de disponibilidad de litros, recargas y predicción de abastecimiento")
@RestController
@RequestMapping("/api/v1/inventory/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Operation(summary = "Crear inventario hídrico", description = "Establece el balance hídrico inicial disponible para un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Inventario creado exitosamente", content = @Content(schema = @Schema(implementation = InventoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros de inventario inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<InventoryResponse> create(@Valid @RequestBody CreateInventoryRequest request) {
        Inventory created = inventoryService.createInventory(
                request.userId(),
                request.availableLiters(),
                request.lastRefillDate(),
                request.nextRefillPrediction()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @Operation(summary = "Obtener inventario por ID", description = "Recupera los datos del balance de inventario por su ID único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventario encontrado", content = @Content(schema = @Schema(implementation = InventoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @GetMapping("/{inventoryId}")
    public ResponseEntity<InventoryResponse> getById(@PathVariable Integer inventoryId) {
        return inventoryService.getById(inventoryId)
                .map(inventory -> ResponseEntity.ok(toResponse(inventory)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener inventario por usuario", description = "Recupera el balance de agua disponible y predicción de recarga para un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventario del usuario obtenido", content = @Content(schema = @Schema(implementation = InventoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "No existe inventario para el usuario")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<InventoryResponse> getByUserId(@PathVariable Integer userId) {
        return inventoryService.getByUserId(userId)
                .map(inventory -> ResponseEntity.ok(toResponse(inventory)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar todos los inventarios", description = "Retorna el inventario hídrico global de todos los usuarios.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista completa de inventarios")
    })
    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAll() {
        List<InventoryResponse> data = inventoryService.getAll().stream().map(this::toResponse).toList();
        return ResponseEntity.ok(data);
    }

    @Operation(summary = "Actualizar inventario", description = "Modifica los litros disponibles o las fechas de recarga.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inventario actualizado exitosamente", content = @Content(schema = @Schema(implementation = InventoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @PutMapping("/{inventoryId}")
    public ResponseEntity<InventoryResponse> update(
            @PathVariable Integer inventoryId,
            @Valid @RequestBody UpdateInventoryRequest request
    ) {
        Inventory updated = inventoryService.updateInventory(
                inventoryId,
                request.availableLiters(),
                request.lastRefillDate(),
                request.nextRefillPrediction()
        );
        return ResponseEntity.ok(toResponse(updated));
    }

    @Operation(summary = "Eliminar inventario", description = "Remueve un registro de inventario del sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Inventario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<Void> delete(@PathVariable Integer inventoryId) {
        inventoryService.deleteById(inventoryId);
        return ResponseEntity.noContent().build();
    }

    private InventoryResponse toResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.inventoryId(),
                inventory.userId(),
                inventory.availableLiters(),
                inventory.lastRefillDate(),
                inventory.nextRefillPrediction()
        );
    }
}
