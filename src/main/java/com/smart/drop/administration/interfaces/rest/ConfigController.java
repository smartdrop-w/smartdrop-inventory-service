package com.smart.drop.administration.interfaces.rest;

import com.smart.drop.administration.application.services.ConfigService;
import com.smart.drop.administration.domain.model.entities.SystemConfiguration;
import com.smart.drop.administration.interfaces.rest.dto.CreateConfigRequest;
import com.smart.drop.administration.interfaces.rest.dto.SystemConfigResponse;
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
 * Controlador REST para administración de parámetros y variables de configuración global del sistema.
 */
@Tag(name = "System Configuration", description = "Endpoints para consulta y ajuste de parámetros globales del entorno")
@RestController
@RequestMapping("/api/v1/administration/configs")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @Operation(summary = "Crear parámetro de configuración", description = "Registra una nueva variable de configuración global.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Configuración creada exitosamente", content = @Content(schema = @Schema(implementation = SystemConfigResponse.class))),
            @ApiResponse(responseCode = "400", description = "Clave o valor inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<SystemConfigResponse> create(@Valid @RequestBody CreateConfigRequest request) {
        SystemConfiguration created = configService.createConfiguration(request.parameterKey(), request.parameterValue());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @Operation(summary = "Crear o actualizar parámetro de configuración (Upsert)", description = "Inserta o modifica el valor de una variable de configuración por su clave.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Configuración actualizada exitosamente", content = @Content(schema = @Schema(implementation = SystemConfigResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    @PutMapping
    public ResponseEntity<SystemConfigResponse> upsert(@Valid @RequestBody CreateConfigRequest request) {
        SystemConfiguration updated = configService.upsertConfiguration(request.parameterKey(), request.parameterValue());
        return ResponseEntity.ok(toResponse(updated));
    }

    @Operation(summary = "Obtener configuración por ID", description = "Recupera un parámetro de configuración por su ID numérico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Configuración encontrada", content = @Content(schema = @Schema(implementation = SystemConfigResponse.class))),
            @ApiResponse(responseCode = "404", description = "Configuración no encontrada")
    })
    @GetMapping("/{configId}")
    public ResponseEntity<SystemConfigResponse> getById(@PathVariable Integer configId) {
        return configService.getById(configId)
                .map(config -> ResponseEntity.ok(toResponse(config)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener configuración por clave", description = "Busca un parámetro por su nombre canónico (ej: TELEMETRY_SAMPLE_INTERVAL_SECONDS).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Configuración encontrada", content = @Content(schema = @Schema(implementation = SystemConfigResponse.class))),
            @ApiResponse(responseCode = "404", description = "Clave de configuración no encontrada")
    })
    @GetMapping("/by-key")
    public ResponseEntity<SystemConfigResponse> getByKey(@RequestParam String parameterKey) {
        return configService.getByParameterKey(parameterKey)
                .map(config -> ResponseEntity.ok(toResponse(config)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar todas las configuraciones", description = "Retorna el catálogo completo de parámetros del sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista completa de configuraciones")
    })
    @GetMapping
    public ResponseEntity<List<SystemConfigResponse>> getAll() {
        List<SystemConfigResponse> data = configService.getAll().stream().map(this::toResponse).toList();
        return ResponseEntity.ok(data);
    }

    private SystemConfigResponse toResponse(SystemConfiguration config) {
        return new SystemConfigResponse(
                config.configId(),
                config.parameterKey(),
                config.parameterValue(),
                config.updatedAt()
        );
    }
}
