package com.smart.drop.administration.infrastructure.persistence.jpa.repositories;

import com.smart.drop.administration.infrastructure.persistence.jpa.entities.SystemConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemConfigJpaRepository extends JpaRepository<SystemConfigEntity, Integer> {

    Optional<SystemConfigEntity> findByParameterKey(String parameterKey);
}

