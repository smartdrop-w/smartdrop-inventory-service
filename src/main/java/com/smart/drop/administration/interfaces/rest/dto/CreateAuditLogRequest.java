package com.smart.drop.administration.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Solicitud para registrar un evento de auditoría")
public record CreateAuditLogRequest(
        @Schema(description = "Identificador del administrador", example = "1")
        @NotNull(message = "adminId is required")
        Integer adminId,

        @Schema(description = "Tipo de acción ejecutada", example = "TANK_REPLENISH")
        @NotBlank(message = "action is required")
        String action,

        @Schema(description = "Detalle descriptivo de la acción", example = "Reabastecimiento manual del Tanque 1 a 2500L")
        String description
) {
}
