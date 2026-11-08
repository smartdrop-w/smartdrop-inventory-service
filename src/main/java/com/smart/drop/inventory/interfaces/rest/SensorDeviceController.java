package com.smart.drop.inventory.interfaces.rest;

import com.smart.drop.inventory.application.services.SensorDeviceService;
import com.smart.drop.inventory.domain.model.entities.SensorDevice;
import com.smart.drop.inventory.interfaces.rest.dto.*;
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
 * Controlador REST para gestión de sensores IoT e ingesta de telemetría normalizada.
 */
@Tag(name = "IoT Sensor Devices & Telemetry", description = "Gestión de hardware de sensado y procesamiento de telemetría normalizada")
@RestController
@RequestMapping("/api/v1/inventory/devices")
public class SensorDeviceController {

    private final SensorDeviceService sensorDeviceService;

    public SensorDeviceController(SensorDeviceService sensorDeviceService) {
        this.sensorDeviceService = sensorDeviceService;
    }

    @Operation(summary = "Ingestar y normalizar telemetría IoT", description = "Recibe lecturas crudas de hardware heterogéneo (ultrasónico, térmico, caudal) y las procesa mediante el Patrón Factory Method, actualizando niveles de tanques y consumos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Telemetría normalizada y registrada con éxito", content = @Content(schema = @Schema(implementation = TelemetryIngestionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Payload de sensor o tipo no soportado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/telemetry")
    public ResponseEntity<TelemetryIngestionResponse> ingestTelemetry(@Valid @RequestBody TelemetryIngestionRequest request) {
        TelemetryIngestionResponse response = sensorDeviceService.processTelemetry(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Registrar nuevo dispositivo sensor", description = "Registra un nuevo sensor IoT asociando usuario, ubicación y parámetros de flujo iniciales.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Dispositivo registrado exitosamente", content = @Content(schema = @Schema(implementation = SensorDeviceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros de dispositivo inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<SensorDeviceResponse> create(@Valid @RequestBody CreateSensorDeviceRequest request) {
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

    @Operation(summary = "Obtener sensor por ID", description = "Recupera la ficha técnica y estado de un dispositivo sensor registrado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dispositivo encontrado", content = @Content(schema = @Schema(implementation = SensorDeviceResponse.class))),
            @ApiResponse(responseCode = "404", description = "Dispositivo no encontrado")
    })
    @GetMapping("/{deviceId}")
    public ResponseEntity<SensorDeviceResponse> getById(@PathVariable Integer deviceId) {
        return sensorDeviceService.getById(deviceId)
                .map(device -> ResponseEntity.ok(toResponse(device)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar sensores por usuario", description = "Retorna todos los sensores instalados para un usuario o cliente específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de sensores recuperada exitosamente")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SensorDeviceResponse>> getByUserId(@PathVariable Integer userId) {
        List<SensorDeviceResponse> data = sensorDeviceService.getByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(data);
    }

    @Operation(summary = "Listar todos los sensores", description = "Retorna la colección completa de sensores desplegados en la red IoT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista global de sensores")
    })
    @GetMapping
    public ResponseEntity<List<SensorDeviceResponse>> getAll() {
        List<SensorDeviceResponse> data = sensorDeviceService.getAll()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(data);
    }

    @Operation(summary = "Actualizar dispositivo sensor", description = "Actualiza los parámetros operativos, ubicación o estado de batería del dispositivo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensor actualizado exitosamente", content = @Content(schema = @Schema(implementation = SensorDeviceResponse.class))),
            @ApiResponse(responseCode = "404", description = "Sensor no encontrado")
    })
    @PutMapping("/{deviceId}")
    public ResponseEntity<SensorDeviceResponse> update(
            @PathVariable Integer deviceId,
            @Valid @RequestBody UpdateSensorDeviceRequest request
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

    @Operation(summary = "Eliminar sensor", description = "Da de baja un dispositivo de la plataforma.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sensor eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Sensor no encontrado")
    })
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
