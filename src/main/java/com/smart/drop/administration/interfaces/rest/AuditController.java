package com.smart.drop.administration.interfaces.rest;

import com.smart.drop.administration.application.services.AuditService;
import com.smart.drop.administration.domain.model.entities.AuditLog;
import com.smart.drop.administration.interfaces.rest.dto.AuditLogResponse;
import com.smart.drop.administration.interfaces.rest.dto.CreateAuditLogRequest;
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
 * Controlador REST para registro y consulta de bitácoras de auditoría del sistema.
 */
@Tag(name = "Audit Logs & Security Traceability", description = "Endpoints para registro y revisión de auditoría sobre acciones administrativas")
@RestController
@RequestMapping("/api/v1/administration/audit-logs")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @Operation(summary = "Registrar evento de auditoría", description = "Registra una acción ejecutada por un operador administrativo para trazabilidad.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Log de auditoría registrado exitosamente", content = @Content(schema = @Schema(implementation = AuditLogResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de log inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<AuditLogResponse> create(@Valid @RequestBody CreateAuditLogRequest request) {
        AuditLog created = auditService.createLog(request.adminId(), request.action(), request.description());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @Operation(summary = "Obtener log por ID", description = "Recupera un evento de auditoría por su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Log de auditoría encontrado", content = @Content(schema = @Schema(implementation = AuditLogResponse.class))),
            @ApiResponse(responseCode = "404", description = "Log no encontrado")
    })
    @GetMapping("/{logId}")
    public ResponseEntity<AuditLogResponse> getById(@PathVariable Integer logId) {
        return auditService.getById(logId)
                .map(log -> ResponseEntity.ok(toResponse(log)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar logs por administrador", description = "Recupera todas las acciones de auditoría ejecutadas por un administrador específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logs recuperados exitosamente")
    })
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<AuditLogResponse>> getByAdminId(@PathVariable Integer adminId) {
        List<AuditLogResponse> data = auditService.getByAdminId(adminId).stream().map(this::toResponse).toList();
        return ResponseEntity.ok(data);
    }

    @Operation(summary = "Listar todos los logs de auditoría", description = "Retorna el historial completo de eventos de auditoría del sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historial completo de auditoría")
    })
    @GetMapping
    public ResponseEntity<List<AuditLogResponse>> getAll() {
        List<AuditLogResponse> data = auditService.getAll().stream().map(this::toResponse).toList();
        return ResponseEntity.ok(data);
    }

    private AuditLogResponse toResponse(AuditLog auditLog) {
        return new AuditLogResponse(
                auditLog.logId(),
                auditLog.adminId(),
                auditLog.action(),
                auditLog.timestamp(),
                auditLog.description()
        );
    }
}
