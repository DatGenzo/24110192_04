package com.thanhdat.exam04.services;

import com.thanhdat.exam04.models.AdminUser_24110192;
import com.thanhdat.exam04.models.UserPage_24110192;

public interface UserService_24110192 {

    UserPage_24110192 findPage(int requestedPage);

    AdminUser_24110192 findByUsername(
            String username
    );

    void create(AdminUser_24110192 user);

    void update(AdminUser_24110192 user);

    void delete(String username);
}
