package com.smart.drop.administration.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Solicitud para registrar un nuevo usuario administrativo")
public record CreateAdminUserRequest(
        @Schema(description = "Nombre de usuario único", example = "admin_supervisor")
        @NotBlank(message = "username is required")
        String username,

        @Schema(description = "Correo electrónico institucional", example = "admin@smartdrop.pe")
        @NotBlank(message = "email is required")
        @Email(message = "Invalid email format")
        String email,

        @Schema(description = "Rol administrativo inicial (SUPER_ADMIN, OPERATOR)", example = "SUPER_ADMIN")
        @NotBlank(message = "role is required")
        String role
) {
}
