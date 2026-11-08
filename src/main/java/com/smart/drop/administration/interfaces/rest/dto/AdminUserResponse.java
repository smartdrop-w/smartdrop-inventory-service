package com.smart.drop.administration.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Datos del usuario administrativo del sistema")
public record AdminUserResponse(
        @Schema(description = "Identificador único del administrador", example = "1")
        Integer adminId,

        @Schema(description = "Nombre de usuario único", example = "admin_supervisor")
        String username,

        @Schema(description = "Correo electrónico institucional", example = "admin@smartdrop.pe")
        String email,

        @Schema(description = "Rol administrativo (SUPER_ADMIN, OPERATOR, AUDITOR)", example = "SUPER_ADMIN")
        String role,

        @Schema(description = "Fecha de alta en la plataforma", example = "2026-11-13T23:30:00")
        LocalDateTime createdAt
) {
}
