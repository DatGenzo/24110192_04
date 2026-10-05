package com.thanhdat.exam04.repositories;

import java.util.List;
import java.util.Optional;

import com.thanhdat.exam04.models.AdminUser_24110192;

public interface UserRepository_24110192 {

    List<AdminUser_24110192> findPage(
            int offset,
            int pageSize
    );

    long countAll();

    Optional<AdminUser_24110192> findByUsername(
            String username
    );

    boolean existsByUsername(String username);

    boolean existsByEmailExceptUsername(
            String email,
            String excludedUsername
    );

    void create(AdminUser_24110192 user);

    void update(
            AdminUser_24110192 user,
            boolean changePassword
    );

    void deleteByUsername(String username);
}
