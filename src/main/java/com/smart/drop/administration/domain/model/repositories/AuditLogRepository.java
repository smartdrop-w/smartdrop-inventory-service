package com.smart.drop.administration.domain.model.repositories;

import com.smart.drop.administration.domain.model.entities.AuditLog;

import java.util.List;
import java.util.Optional;

public interface AuditLogRepository {

    AuditLog save(AuditLog auditLog);

    Optional<AuditLog> findById(Integer logId);

    List<AuditLog> findByAdminId(Integer adminId);

    List<AuditLog> findAll();
}

