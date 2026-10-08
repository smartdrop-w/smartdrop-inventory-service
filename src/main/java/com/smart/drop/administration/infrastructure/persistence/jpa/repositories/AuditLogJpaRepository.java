package com.smart.drop.administration.infrastructure.persistence.jpa.repositories;

import com.smart.drop.administration.infrastructure.persistence.jpa.entities.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogJpaRepository extends JpaRepository<AuditLogEntity, Integer> {

    List<AuditLogEntity> findByAdmin_AdminId(Integer adminId);
}

