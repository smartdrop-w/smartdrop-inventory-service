package com.smart.drop.administration.interfaces.rest;

import com.smart.drop.administration.application.services.ConfigService;
import com.smart.drop.administration.domain.model.entities.SystemConfiguration;
import com.smart.drop.administration.interfaces.rest.dto.CreateConfigRequest;
import com.smart.drop.administration.interfaces.rest.dto.SystemConfigResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/administration/configs")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @PostMapping
    public ResponseEntity<SystemConfigResponse> create(@RequestBody CreateConfigRequest request) {
        SystemConfiguration created = configService.createConfiguration(request.parameterKey(), request.parameterValue());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @PutMapping
    public ResponseEntity<SystemConfigResponse> upsert(@RequestBody CreateConfigRequest request) {
        SystemConfiguration updated = configService.upsertConfiguration(request.parameterKey(), request.parameterValue());
        return ResponseEntity.ok(toResponse(updated));
    }

    @GetMapping("/{configId}")
    public ResponseEntity<SystemConfigResponse> getById(@PathVariable Integer configId) {
        return configService.getById(configId)
                .map(config -> ResponseEntity.ok(toResponse(config)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/by-key")
    public ResponseEntity<SystemConfigResponse> getByKey(@RequestParam String parameterKey) {
        return configService.getByParameterKey(parameterKey)
                .map(config -> ResponseEntity.ok(toResponse(config)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<SystemConfigResponse>> getAll() {
        List<SystemConfigResponse> data = configService.getAll().stream().map(this::toResponse).toList();
        return ResponseEntity.ok(data);
    }

    private SystemConfigResponse toResponse(SystemConfiguration configuration) {
        return new SystemConfigResponse(
                configuration.configId(),
                configuration.parameterKey(),
                configuration.parameterValue(),
                configuration.updatedAt()
        );
    }
}

