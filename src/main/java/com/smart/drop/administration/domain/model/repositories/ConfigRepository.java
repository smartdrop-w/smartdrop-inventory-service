package com.smart.drop.administration.domain.model.repositories;

import com.smart.drop.administration.domain.model.entities.SystemConfiguration;

import java.util.List;
import java.util.Optional;

public interface ConfigRepository {

    SystemConfiguration save(SystemConfiguration configuration);

    Optional<SystemConfiguration> findById(Integer configId);

    Optional<SystemConfiguration> findByParameterKey(String parameterKey);

    List<SystemConfiguration> findAll();
}

