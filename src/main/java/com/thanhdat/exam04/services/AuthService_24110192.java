package com.thanhdat.exam04.services;

import com.thanhdat.exam04.models.User_24110192;

public interface AuthService_24110192 {

    void registerPending(
            String username,
            String password,
            String confirmPassword,
            String phone,
            String fullName,
            String email
    );

    User_24110192 authenticate(
            String username,
            String password
    );

    void activate(String username);
}
