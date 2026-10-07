package com.smart.drop.inventory.interfaces.rest;

import com.smart.drop.inventory.application.services.InventoryService;
import com.smart.drop.inventory.domain.model.entities.Inventory;
import com.smart.drop.inventory.interfaces.rest.dto.CreateInventoryRequest;
import com.smart.drop.inventory.interfaces.rest.dto.InventoryResponse;
import com.smart.drop.inventory.interfaces.rest.dto.UpdateInventoryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> create(@RequestBody CreateInventoryRequest request) {
        Inventory created = inventoryService.createInventory(
                request.userId(),
                request.availableLiters(),
                request.lastRefillDate(),
                request.nextRefillPrediction()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @GetMapping("/{inventoryId}")
    public ResponseEntity<InventoryResponse> getById(@PathVariable Integer inventoryId) {
        return inventoryService.getById(inventoryId)
                .map(inventory -> ResponseEntity.ok(toResponse(inventory)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<InventoryResponse> getByUserId(@PathVariable Integer userId) {
        return inventoryService.getByUserId(userId)
                .map(inventory -> ResponseEntity.ok(toResponse(inventory)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAll() {
        List<InventoryResponse> data = inventoryService.getAll().stream().map(this::toResponse).toList();
        return ResponseEntity.ok(data);
    }

    @PutMapping("/{inventoryId}")
    public ResponseEntity<InventoryResponse> update(
            @PathVariable Integer inventoryId,
            @RequestBody UpdateInventoryRequest request
    ) {
        Inventory updated = inventoryService.updateInventory(
                inventoryId,
                request.availableLiters(),
                request.lastRefillDate(),
                request.nextRefillPrediction()
        );
        return ResponseEntity.ok(toResponse(updated));
    }

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

