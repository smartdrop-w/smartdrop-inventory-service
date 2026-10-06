package com.smart.drop.administration.application.services;

import com.smart.drop.administration.domain.model.entities.SystemConfiguration;
import com.smart.drop.administration.domain.model.repositories.ConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ConfigService {

    private final ConfigRepository configRepository;

    public ConfigService(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public SystemConfiguration createConfiguration(String parameterKey, String parameterValue) {
        return configRepository.save(SystemConfiguration.create(parameterKey, parameterValue));
    }

    public SystemConfiguration upsertConfiguration(String parameterKey, String parameterValue) {
        SystemConfiguration current = configRepository.findByParameterKey(parameterKey)
                .orElse(null);
        if (current == null) {
            return configRepository.save(SystemConfiguration.create(parameterKey, parameterValue));
        }
        return configRepository.save(current.withValue(parameterValue));
    }

    @Transactional(readOnly = true)
    public Optional<SystemConfiguration> getById(Integer configId) {
        return configRepository.findById(configId);
    }

    @Transactional(readOnly = true)
    public Optional<SystemConfiguration> getByParameterKey(String parameterKey) {
        return configRepository.findByParameterKey(parameterKey);
    }

    @Transactional(readOnly = true)
    public List<SystemConfiguration> getAll() {
        return configRepository.findAll();
    }
}

