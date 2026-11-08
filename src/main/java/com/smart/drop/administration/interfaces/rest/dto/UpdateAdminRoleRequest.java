package com.smart.drop.administration.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Solicitud para actualizar el rol de un administrador")
public record UpdateAdminRoleRequest(
        @Schema(description = "Nuevo rol administrativo", example = "OPERATOR")
        @NotBlank(message = "role is required")
        String role
) {
}
