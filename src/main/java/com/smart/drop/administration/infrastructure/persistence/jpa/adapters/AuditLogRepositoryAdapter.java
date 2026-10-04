package com.smart.drop.administration.infrastructure.persistence.jpa.adapters;

import com.smart.drop.administration.domain.model.entities.AuditLog;
import com.smart.drop.administration.domain.model.repositories.AuditLogRepository;
import com.smart.drop.administration.infrastructure.persistence.jpa.entities.AdminUserEntity;
import com.smart.drop.administration.infrastructure.persistence.jpa.entities.AuditLogEntity;
import com.smart.drop.administration.infrastructure.persistence.jpa.repositories.AdminUserJpaRepository;
import com.smart.drop.administration.infrastructure.persistence.jpa.repositories.AuditLogJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class AuditLogRepositoryAdapter implements AuditLogRepository {

    private final AuditLogJpaRepository auditLogJpaRepository;
    private final AdminUserJpaRepository adminUserJpaRepository;

    public AuditLogRepositoryAdapter(AuditLogJpaRepository auditLogJpaRepository,
                                     AdminUserJpaRepository adminUserJpaRepository) {
        this.auditLogJpaRepository = auditLogJpaRepository;
        this.adminUserJpaRepository = adminUserJpaRepository;
    }

    @Override
    public AuditLog save(AuditLog auditLog) {
        AuditLogEntity entity = new AuditLogEntity();
        entity.setLogId(auditLog.logId());
        entity.setAction(auditLog.action());
        entity.setDescription(auditLog.description());
        entity.setTimestamp(auditLog.timestamp() == null ? LocalDateTime.now() : auditLog.timestamp());
        entity.setAdmin(resolveAdmin(auditLog.adminId()));

        AuditLogEntity saved = auditLogJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<AuditLog> findById(Integer logId) {
        return auditLogJpaRepository.findById(logId).map(this::toDomain);
    }

    @Override
    public List<AuditLog> findByAdminId(Integer adminId) {
        return auditLogJpaRepository.findByAdmin_AdminId(adminId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<AuditLog> findAll() {
        return auditLogJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    private AdminUserEntity resolveAdmin(Integer adminId) {
        if (adminId == null) {
            return null;
        }
        return adminUserJpaRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found: " + adminId));
    }

    private AuditLog toDomain(AuditLogEntity entity) {
        return new AuditLog(
                entity.getLogId(),
                entity.getAdmin() == null ? null : entity.getAdmin().getAdminId(),
                entity.getAction(),
                entity.getTimestamp(),
                entity.getDescription()
        );
    }
}

