package com.smart.drop.administration.interfaces.rest;

import com.smart.drop.administration.application.services.AdminManagementService;
import com.smart.drop.administration.domain.model.entities.AdminUser;
import com.smart.drop.administration.interfaces.rest.dto.AdminUserResponse;
import com.smart.drop.administration.interfaces.rest.dto.CreateAdminUserRequest;
import com.smart.drop.administration.interfaces.rest.dto.UpdateAdminRoleRequest;
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
 * Controlador REST para administración de usuarios internos y operadores de la plataforma SmartDrop.
 */
@Tag(name = "Platform Administration & Staff", description = "Endpoints para registro y gestión de privilegios del personal administrativo y operadores")
@RestController
@RequestMapping("/api/v1/administration/admins")
public class AdminController {

	private final AdminManagementService adminManagementService;

	public AdminController(AdminManagementService adminManagementService) {
		this.adminManagementService = adminManagementService;
	}

	@Operation(summary = "Crear nuevo usuario administrador", description = "Registra un nuevo operador administrativo en la plataforma.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Administrador creado exitosamente", content = @Content(schema = @Schema(implementation = AdminUserResponse.class))),
			@ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor")
	})
	@PostMapping
	public ResponseEntity<AdminUserResponse> create(@Valid @RequestBody CreateAdminUserRequest request) {
		AdminUser created = adminManagementService.createAdminUser(
				request.username(),
				request.email(),
				request.role()
		);
		return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
	}

	@Operation(summary = "Actualizar rol administrativo", description = "Modifica los privilegios y nivel de acceso de un administrador.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente", content = @Content(schema = @Schema(implementation = AdminUserResponse.class))),
			@ApiResponse(responseCode = "404", description = "Administrador no encontrado")
	})
	@PutMapping("/{adminId}/role")
	public ResponseEntity<AdminUserResponse> updateRole(@PathVariable Integer adminId,
														@Valid @RequestBody UpdateAdminRoleRequest request) {
		AdminUser updated = adminManagementService.updateAdminRole(adminId, request.role());
		return ResponseEntity.ok(toResponse(updated));
	}

	@Operation(summary = "Obtener administrador por ID", description = "Recupera la información de un administrador específico.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Administrador encontrado", content = @Content(schema = @Schema(implementation = AdminUserResponse.class))),
			@ApiResponse(responseCode = "404", description = "Administrador no encontrado")
	})
	@GetMapping("/{adminId}")
	public ResponseEntity<AdminUserResponse> getById(@PathVariable Integer adminId) {
		return adminManagementService.getById(adminId)
				.map(admin -> ResponseEntity.ok(toResponse(admin)))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@Operation(summary = "Listar todos los administradores", description = "Retorna el personal administrativo registrado en la plataforma.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Lista completa de administradores")
	})
	@GetMapping
	public ResponseEntity<List<AdminUserResponse>> getAll() {
		List<AdminUserResponse> data = adminManagementService.getAll().stream()
				.map(this::toResponse)
				.toList();
		return ResponseEntity.ok(data);
	}

	private AdminUserResponse toResponse(AdminUser adminUser) {
		return new AdminUserResponse(
				adminUser.adminId(),
				adminUser.username(),
				adminUser.email(),
				adminUser.role(),
				adminUser.createdAt()
		);
	}
}
