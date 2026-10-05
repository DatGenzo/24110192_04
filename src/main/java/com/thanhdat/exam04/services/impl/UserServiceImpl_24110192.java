package com.thanhdat.exam04.services.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import com.thanhdat.exam04.models.AdminUser_24110192;
import com.thanhdat.exam04.models.UserPage_24110192;
import com.thanhdat.exam04.repositories.UserRepository_24110192;
import com.thanhdat.exam04.services.UserService_24110192;

public class UserServiceImpl_24110192
        implements UserService_24110192 {

    public static final int PAGE_SIZE = 6;

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("[A-Za-z0-9._-]{3,50}");

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
            );

    private final UserRepository_24110192 repository;

    public UserServiceImpl_24110192(
            UserRepository_24110192 repository
    ) {
        this.repository = repository;
    }

    @Override
    public UserPage_24110192 findPage(
            int requestedPage
    ) {
        long totalItems = repository.countAll();

        int totalPages = (int) Math.max(
                1,
                Math.ceil(
                        (double) totalItems / PAGE_SIZE
                )
        );

        int page = Math.max(
                1,
                Math.min(requestedPage, totalPages)
        );

        int offset = (page - 1) * PAGE_SIZE;

        List<AdminUser_24110192> items =
                repository.findPage(
                        offset,
                        PAGE_SIZE
                );

        return new UserPage_24110192(
                items,
                page,
                PAGE_SIZE,
                totalPages,
                totalItems
        );
    }

    @Override
    public AdminUser_24110192 findByUsername(
            String username
    ) {
        if (isBlank(username)) {
            throw new IllegalArgumentException(
                    "Username không hợp lệ"
            );
        }

        return repository
                .findByUsername(username.trim())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy User: "
                                        + username
                        )
                );
    }

    @Override
    public void create(AdminUser_24110192 user) {
        normalize(user);
        validate(user, true);

        if (repository.existsByUsername(
                user.getUsername()
        )) {
            throw new IllegalArgumentException(
                    "Username đã tồn tại"
            );
        }

        if (repository.existsByEmailExceptUsername(
                user.getEmail(),
                null
        )) {
            throw new IllegalArgumentException(
                    "Email đã tồn tại"
            );
        }

        user.setPassword(
                hashPassword(user.getPassword())
        );

        repository.create(user);
    }

    @Override
    public void update(AdminUser_24110192 user) {
        normalize(user);
        validate(user, false);

        AdminUser_24110192 existing =
                findByUsername(user.getUsername());

        if (repository.existsByEmailExceptUsername(
                user.getEmail(),
                user.getUsername()
        )) {
            throw new IllegalArgumentException(
                    "Email đã được User khác sử dụng"
            );
        }

        boolean changePassword =
                !isBlank(user.getPassword());

        if (changePassword) {
            user.setPassword(
                    hashPassword(user.getPassword())
            );
        }
        else {
            user.setPassword(
                    existing.getPassword()
            );
        }

        repository.update(
                user,
                changePassword
        );
    }

    @Override
    public void delete(String username) {
        AdminUser_24110192 existing =
                findByUsername(username);

        repository.deleteByUsername(
                existing.getUsername()
        );
    }

    private void validate(
            AdminUser_24110192 user,
            boolean creating
    ) {
        if (isBlank(user.getUsername())
                || !USERNAME_PATTERN
                        .matcher(user.getUsername())
                        .matches()) {

            throw new IllegalArgumentException(
                    "Username phải có từ 3 đến 50 ký tự "
                            + "và chỉ gồm chữ, số, dấu chấm, "
                            + "gạch dưới hoặc gạch ngang"
            );
        }

        if (creating && isBlank(user.getPassword())) {
            throw new IllegalArgumentException(
                    "Mật khẩu không được để trống"
            );
        }

        if (!isBlank(user.getPassword())
                && user.getPassword().length() < 6) {

            throw new IllegalArgumentException(
                    "Mật khẩu phải có ít nhất 6 ký tự"
            );
        }

        if (isBlank(user.getFullName())) {
            throw new IllegalArgumentException(
                    "Họ tên không được để trống"
            );
        }

        if (user.getFullName().length() > 100) {
            throw new IllegalArgumentException(
                    "Họ tên không được vượt quá 100 ký tự"
            );
        }

        if (isBlank(user.getEmail())
                || !EMAIL_PATTERN
                        .matcher(user.getEmail())
                        .matches()) {

            throw new IllegalArgumentException(
                    "Email không đúng định dạng"
            );
        }

        if (user.getEmail().length() > 150) {
            throw new IllegalArgumentException(
                    "Email không được vượt quá 150 ký tự"
            );
        }

        if (user.getPhone() != null
                && user.getPhone().length() > 20) {

            throw new IllegalArgumentException(
                    "Số điện thoại không được vượt quá 20 ký tự"
            );
        }
    }

    private void normalize(AdminUser_24110192 user) {
        if (user.getUsername() != null) {
            user.setUsername(
                    user.getUsername()
                            .trim()
                            .toLowerCase(Locale.ROOT)
            );
        }

        if (user.getFullName() != null) {
            user.setFullName(
                    user.getFullName().trim()
            );
        }

        if (user.getEmail() != null) {
            user.setEmail(
                    user.getEmail()
                            .trim()
                            .toLowerCase(Locale.ROOT)
            );
        }

        if (user.getPhone() != null) {
            user.setPhone(
                    user.getPhone().trim()
            );
        }

        if (user.getImages() != null) {
            user.setImages(
                    user.getImages().trim()
            );
        }
    }

    private String hashPassword(String rawPassword) {
        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    rawPassword.getBytes(
                            StandardCharsets.UTF_8
                    )
            );

            StringBuilder result =
                    new StringBuilder();

            for (byte value : hash) {
                result.append(
                        String.format("%02X", value)
                );
            }

            return result.toString();
        }
        catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "Không thể mã hóa mật khẩu",
                    exception
            );
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
