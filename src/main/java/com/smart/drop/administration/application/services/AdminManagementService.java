package com.smart.drop.administration.application.services;

import com.smart.drop.administration.domain.model.entities.AdminUser;
import com.smart.drop.administration.domain.model.repositories.AdminUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminManagementService {

    private final AdminUserRepository adminUserRepository;

    public AdminManagementService(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    public AdminUser createAdminUser(String username, String email, String role) {
        return adminUserRepository.save(AdminUser.create(username, email, role));
    }

    public AdminUser updateAdminRole(Integer adminId, String role) {
        AdminUser current = adminUserRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found: " + adminId));
        return adminUserRepository.save(current.withRole(role));
    }

    @Transactional(readOnly = true)
    public Optional<AdminUser> getById(Integer adminId) {
        return adminUserRepository.findById(adminId);
    }

    @Transactional(readOnly = true)
    public List<AdminUser> getAll() {
        return adminUserRepository.findAll();
    }
}

