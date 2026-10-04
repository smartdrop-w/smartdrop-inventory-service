package com.smart.drop.administration.infrastructure.persistence.jpa.adapters;

import com.smart.drop.administration.domain.model.entities.AdminUser;
import com.smart.drop.administration.domain.model.repositories.AdminUserRepository;
import com.smart.drop.administration.infrastructure.persistence.jpa.entities.AdminUserEntity;
import com.smart.drop.administration.infrastructure.persistence.jpa.repositories.AdminUserJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class AdminUserRepositoryAdapter implements AdminUserRepository {

    private final AdminUserJpaRepository adminUserJpaRepository;

    public AdminUserRepositoryAdapter(AdminUserJpaRepository adminUserJpaRepository) {
        this.adminUserJpaRepository = adminUserJpaRepository;
    }

    @Override
    public AdminUser save(AdminUser adminUser) {
        AdminUserEntity entity = new AdminUserEntity();
        entity.setAdminId(adminUser.adminId());
        entity.setUsername(adminUser.username());
        entity.setEmail(adminUser.email());
        entity.setRole(adminUser.role());
        entity.setCreatedAt(adminUser.createdAt() == null ? LocalDateTime.now() : adminUser.createdAt());
        AdminUserEntity saved = adminUserJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<AdminUser> findById(Integer adminId) {
        return adminUserJpaRepository.findById(adminId).map(this::toDomain);
    }

    @Override
    public List<AdminUser> findAll() {
        return adminUserJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    private AdminUser toDomain(AdminUserEntity entity) {
        return new AdminUser(entity.getAdminId(), entity.getUsername(), entity.getEmail(), entity.getRole(), entity.getCreatedAt());
    }
}

