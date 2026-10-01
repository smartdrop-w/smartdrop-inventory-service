package com.smart.drop.administration.domain.model.repositories;

import com.smart.drop.administration.domain.model.entities.AdminUser;

import java.util.List;
import java.util.Optional;

public interface AdminUserRepository {

    AdminUser save(AdminUser adminUser);

    Optional<AdminUser> findById(Integer adminId);

    List<AdminUser> findAll();
}

