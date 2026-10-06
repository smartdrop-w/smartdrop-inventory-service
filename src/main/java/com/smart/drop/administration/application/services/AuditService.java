package com.smart.drop.administration.application.services;

import com.smart.drop.administration.domain.model.entities.AuditLog;
import com.smart.drop.administration.domain.model.repositories.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public AuditLog createLog(Integer adminId, String action, String description) {
        return auditLogRepository.save(AuditLog.create(adminId, action, description));
    }

    @Transactional(readOnly = true)
    public Optional<AuditLog> getById(Integer logId) {
        return auditLogRepository.findById(logId);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getByAdminId(Integer adminId) {
        return auditLogRepository.findByAdminId(adminId);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getAll() {
        return auditLogRepository.findAll();
    }
}

