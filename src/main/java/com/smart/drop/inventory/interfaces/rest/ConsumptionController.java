package com.smart.drop.inventory.interfaces.rest;

import com.smart.drop.inventory.application.services.ConsumptionService;
import com.smart.drop.inventory.domain.model.entities.Consumption;
import com.smart.drop.inventory.interfaces.rest.dto.ConsumptionResponse;
import com.smart.drop.inventory.interfaces.rest.dto.RegisterConsumptionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory/consumptions")
public class ConsumptionController {

    private final ConsumptionService consumptionService;

    public ConsumptionController(ConsumptionService consumptionService) {
        this.consumptionService = consumptionService;
    }

    @PostMapping
    public ResponseEntity<ConsumptionResponse> register(@RequestBody RegisterConsumptionRequest request) {
        Consumption created = consumptionService.registerConsumption(
                request.userId(),
                request.volumeUsed(),
                request.usageContext()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @GetMapping("/{consumptionId}")
    public ResponseEntity<ConsumptionResponse> getById(@PathVariable Integer consumptionId) {
        return consumptionService.getById(consumptionId)
                .map(consumption -> ResponseEntity.ok(toResponse(consumption)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ConsumptionResponse>> getByUserId(@PathVariable Integer userId) {
        List<ConsumptionResponse> data = consumptionService.getByUserId(userId).stream().map(this::toResponse).toList();
        return ResponseEntity.ok(data);
    }

    @GetMapping
    public ResponseEntity<List<ConsumptionResponse>> getAll() {
        List<ConsumptionResponse> data = consumptionService.getAll().stream().map(this::toResponse).toList();
        return ResponseEntity.ok(data);
    }

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
                consumption.dateRecorded(),
                consumption.usageContext()
        );
    }
}

