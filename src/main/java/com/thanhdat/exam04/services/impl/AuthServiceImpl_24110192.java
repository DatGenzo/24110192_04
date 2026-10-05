package com.thanhdat.exam04.services.impl;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

import com.thanhdat.exam04.daos.UserDao_24110192;
import com.thanhdat.exam04.daos.impl.UserDaoImpl_24110192;
import com.thanhdat.exam04.models.User_24110192;
import com.thanhdat.exam04.services.AuthService_24110192;
import com.thanhdat.exam04.utils.PasswordUtil_24110192;

public class AuthServiceImpl_24110192
        implements AuthService_24110192 {

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile(
                    "^[A-Za-z0-9._-]{4,50}$"
            );

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"
            );

    private static final Pattern PHONE_PATTERN =
            Pattern.compile(
                    "^[0-9]{9,15}$"
            );

    private final UserDao_24110192 userDao;

    public AuthServiceImpl_24110192() {
        this.userDao =
                new UserDaoImpl_24110192();
    }

    @Override
    public void registerPending(
            String username,
            String password,
            String confirmPassword,
            String phone,
            String fullName,
            String email
    ) {
        String normalizedUsername =
                normalize(username).toLowerCase(
                        Locale.ROOT
                );

        String normalizedEmail =
                normalize(email).toLowerCase(
                        Locale.ROOT
                );

        String normalizedPhone = normalize(phone);
        String normalizedFullName = normalize(fullName);

        validateRegistration(
                normalizedUsername,
                password,
                confirmPassword,
                normalizedPhone,
                normalizedFullName,
                normalizedEmail
        );

        Optional<User_24110192> existingUsername =
                userDao.findByUsername(
                        normalizedUsername
                );

        if (
                existingUsername.isPresent()
                && existingUsername.get().isActive()
        ) {
            throw new IllegalArgumentException(
                    "Username đã tồn tại"
            );
        }

        Optional<User_24110192> existingEmail =
                userDao.findByEmail(
                        normalizedEmail
                );

        if (
                existingEmail.isPresent()
                && !existingEmail
                        .get()
                        .getUsername()
                        .equalsIgnoreCase(
                                normalizedUsername
                        )
        ) {
            throw new IllegalArgumentException(
                    "Email đã được sử dụng"
            );
        }

        User_24110192 user = new User_24110192();

        user.setUsername(normalizedUsername);
        user.setPassword(
                PasswordUtil_24110192.hash(password)
        );
        user.setPhone(normalizedPhone);
        user.setFullName(normalizedFullName);
        user.setEmail(normalizedEmail);
        user.setAdmin(false);
        user.setActive(false);

        userDao.savePending(user);
    }

    @Override
    public User_24110192 authenticate(
            String username,
            String password
    ) {
        String normalizedUsername =
                normalize(username);

        if (
                normalizedUsername.isBlank()
                || password == null
                || password.isBlank()
        ) {
            throw new IllegalArgumentException(
                    "Vui lòng nhập username và mật khẩu"
            );
        }

        User_24110192 user = userDao
                .findByUsername(normalizedUsername)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Username hoặc mật khẩu không đúng"
                        )
                );

        if (
                !PasswordUtil_24110192.matches(
                        password,
                        user.getPassword()
                )
        ) {
            throw new IllegalArgumentException(
                    "Username hoặc mật khẩu không đúng"
            );
        }

        if (!user.isActive()) {
            throw new IllegalArgumentException(
                    "Tài khoản chưa được kích hoạt"
            );
        }

        return user;
    }

    @Override
    public void activate(String username) {
        String normalizedUsername =
                normalize(username);

        if (normalizedUsername.isBlank()) {
            throw new IllegalArgumentException(
                    "Không xác định được tài khoản"
            );
        }

        userDao.activate(normalizedUsername);
    }

    private void validateRegistration(
            String username,
            String password,
            String confirmPassword,
            String phone,
            String fullName,
            String email
    ) {
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new IllegalArgumentException(
                    "Username phải có 4-50 ký tự, chỉ gồm chữ, số, dấu chấm, gạch dưới hoặc gạch ngang"
            );
        }

        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException(
                    "Mật khẩu phải có ít nhất 6 ký tự"
            );
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException(
                    "Xác nhận mật khẩu không khớp"
            );
        }

        if (
                fullName.isBlank()
                || fullName.length() > 50
        ) {
            throw new IllegalArgumentException(
                    "Họ tên phải có từ 1 đến 50 ký tự"
            );
        }

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException(
                    "Số điện thoại phải có từ 9 đến 15 chữ số"
            );
        }

        if (
                email.length() > 150
                || !EMAIL_PATTERN.matcher(email).matches()
        ) {
            throw new IllegalArgumentException(
                    "Email không hợp lệ"
            );
        }
    }

    private String normalize(String value) {
        return value == null
                ? ""
                : value.trim();
    }
}
