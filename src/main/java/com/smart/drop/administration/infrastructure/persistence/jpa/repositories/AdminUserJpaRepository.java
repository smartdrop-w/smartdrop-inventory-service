package com.smart.drop.administration.infrastructure.persistence.jpa.repositories;

import com.smart.drop.administration.infrastructure.persistence.jpa.entities.AdminUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserJpaRepository extends JpaRepository<AdminUserEntity, Integer> {
}

