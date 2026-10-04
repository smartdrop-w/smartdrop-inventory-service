package com.smart.drop.administration.infrastructure.persistence.jpa.adapters;

import com.smart.drop.administration.domain.model.entities.SystemConfiguration;
import com.smart.drop.administration.domain.model.repositories.ConfigRepository;
import com.smart.drop.administration.infrastructure.persistence.jpa.entities.SystemConfigEntity;
import com.smart.drop.administration.infrastructure.persistence.jpa.repositories.SystemConfigJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ConfigRepositoryAdapter implements ConfigRepository {

    private final SystemConfigJpaRepository systemConfigJpaRepository;

    public ConfigRepositoryAdapter(SystemConfigJpaRepository systemConfigJpaRepository) {
        this.systemConfigJpaRepository = systemConfigJpaRepository;
    }

    @Override
    public SystemConfiguration save(SystemConfiguration configuration) {
        SystemConfigEntity entity = new SystemConfigEntity();
        entity.setConfigId(configuration.configId());
        entity.setParameterKey(configuration.parameterKey());
        entity.setParameterValue(configuration.parameterValue());
        entity.setUpdatedAt(configuration.updatedAt() == null ? LocalDateTime.now() : configuration.updatedAt());
        SystemConfigEntity saved = systemConfigJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<SystemConfiguration> findById(Integer configId) {
        return systemConfigJpaRepository.findById(configId).map(this::toDomain);
    }

    @Override
    public Optional<SystemConfiguration> findByParameterKey(String parameterKey) {
        return systemConfigJpaRepository.findByParameterKey(parameterKey).map(this::toDomain);
    }

    @Override
    public List<SystemConfiguration> findAll() {
        return systemConfigJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    private SystemConfiguration toDomain(SystemConfigEntity entity) {
        return new SystemConfiguration(entity.getConfigId(), entity.getParameterKey(), entity.getParameterValue(), entity.getUpdatedAt());
    }
}

