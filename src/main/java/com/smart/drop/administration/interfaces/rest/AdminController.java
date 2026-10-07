package com.smart.drop.administration.interfaces.rest;

import com.smart.drop.administration.application.services.AdminManagementService;
import com.smart.drop.administration.domain.model.entities.AdminUser;
import com.smart.drop.administration.interfaces.rest.dto.AdminUserResponse;
import com.smart.drop.administration.interfaces.rest.dto.CreateAdminUserRequest;
import com.smart.drop.administration.interfaces.rest.dto.UpdateAdminRoleRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/administration/admins")
public class AdminController {

	private final AdminManagementService adminManagementService;

	public AdminController(AdminManagementService adminManagementService) {
		this.adminManagementService = adminManagementService;
	}

	@PostMapping
	public ResponseEntity<AdminUserResponse> create(@RequestBody CreateAdminUserRequest request) {
		AdminUser created = adminManagementService.createAdminUser(
				request.username(),
				request.email(),
				request.role()
		);
		return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
	}

	@PutMapping("/{adminId}/role")
	public ResponseEntity<AdminUserResponse> updateRole(@PathVariable Integer adminId,
														@RequestBody UpdateAdminRoleRequest request) {
		AdminUser updated = adminManagementService.updateAdminRole(adminId, request.role());
		return ResponseEntity.ok(toResponse(updated));
	}

	@GetMapping("/{adminId}")
	public ResponseEntity<AdminUserResponse> getById(@PathVariable Integer adminId) {
		return adminManagementService.getById(adminId)
				.map(admin -> ResponseEntity.ok(toResponse(admin)))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

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

