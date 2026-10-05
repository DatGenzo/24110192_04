package com.thanhdat.exam04.daos;

import java.util.Optional;

import com.thanhdat.exam04.models.User_24110192;

public interface UserDao_24110192 {

    Optional<User_24110192> findByUsername(
            String username
    );

    Optional<User_24110192> findByEmail(
            String email
    );

    void savePending(User_24110192 user);

    void activate(String username);
}
