package com.smart.drop.administration.interfaces.rest;

import com.smart.drop.administration.application.services.AuditService;
import com.smart.drop.administration.domain.model.entities.AuditLog;
import com.smart.drop.administration.interfaces.rest.dto.AuditLogResponse;
import com.smart.drop.administration.interfaces.rest.dto.CreateAuditLogRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/administration/audit-logs")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @PostMapping
    public ResponseEntity<AuditLogResponse> create(@RequestBody CreateAuditLogRequest request) {
        AuditLog created = auditService.createLog(request.adminId(), request.action(), request.description());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @GetMapping("/{logId}")
    public ResponseEntity<AuditLogResponse> getById(@PathVariable Integer logId) {
        return auditService.getById(logId)
                .map(log -> ResponseEntity.ok(toResponse(log)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<AuditLogResponse>> getByAdminId(@PathVariable Integer adminId) {
        List<AuditLogResponse> data = auditService.getByAdminId(adminId).stream().map(this::toResponse).toList();
        return ResponseEntity.ok(data);
    }

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

