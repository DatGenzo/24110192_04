package com.thanhdat.exam04.controllers;

import java.io.IOException;

import com.thanhdat.exam04.models.User_24110192;
import com.thanhdat.exam04.services.AuthService_24110192;
import com.thanhdat.exam04.services.MailService_24110192;
import com.thanhdat.exam04.services.impl.AuthServiceImpl_24110192;
import com.thanhdat.exam04.services.impl.SmtpMailService_24110192;
import com.thanhdat.exam04.utils.OtpGenerator_24110192;
import com.thanhdat.exam04.utils.PasswordUtil_24110192;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(
        name = "AuthController_24110192",
        urlPatterns = {
                "/login",
                "/register",
                "/verify-otp",
                "/logout"
        }
)
public class AuthController_24110192
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final long OTP_VALIDITY_MILLIS =
            5L * 60L * 1000L;

    private static final int MAX_OTP_ATTEMPTS = 5;

    private AuthService_24110192 authService;
    private MailService_24110192 mailService;

    @Override
    public void init() {
        authService =
                new AuthServiceImpl_24110192();

        mailService =
                new SmtpMailService_24110192();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        switch (request.getServletPath()) {
            case "/login" ->
                    showLogin(request, response);

            case "/register" ->
                    showRegister(request, response);

            case "/verify-otp" ->
                    showVerifyOtp(request, response);

            case "/logout" ->
                    logout(request, response);

            default ->
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND
                    );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        switch (request.getServletPath()) {
            case "/login" ->
                    login(request, response);

            case "/register" ->
                    register(request, response);

            case "/verify-otp" ->
                    verifyOtp(request, response);

            default ->
                    response.sendError(
                            HttpServletResponse
                                    .SC_METHOD_NOT_ALLOWED
                    );
        }
    }

    private void showLogin(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session != null) {
            User_24110192 currentUser =
                    (User_24110192)
                            session.getAttribute(
                                    "currentUser"
                            );

            if (currentUser != null) {
                redirectAfterLogin(
                        currentUser,
                        request,
                        response
                );

                return;
            }

            Object flashSuccess =
                    session.getAttribute(
                            "flashSuccess"
                    );

            if (flashSuccess != null) {
                request.setAttribute(
                        "successMessage",
                        flashSuccess
                );

                session.removeAttribute(
                        "flashSuccess"
                );
            }
        }

        if ("1".equals(
                request.getParameter("forbidden")
        )) {
            request.setAttribute(
                    "errorMessage",
                    "Bạn cần đăng nhập bằng tài khoản Admin"
            );
        }

        if ("1".equals(
                request.getParameter("userRequired")
        )) {
            request.setAttribute(
                    "errorMessage",
                    "Vui lòng đăng nhập bằng tài khoản User để sử dụng giỏ hàng và đặt hàng"
            );
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/login.jsp"
        ).forward(request, response);
    }

    private void showRegister(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/register.jsp"
        ).forward(request, response);
    }

    private void showVerifyOtp(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (
                session == null
                || session.getAttribute(
                        "pendingUsername"
                ) == null
        ) {
            response.sendRedirect(
                    request.getContextPath()
                            + "/register"
            );

            return;
        }

        prepareOtpView(session, request);

        request.getRequestDispatcher(
                "/WEB-INF/views/verify-otp.jsp"
        ).forward(request, response);
    }

    private void register(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String username =
                request.getParameter("username");

        String password =
                request.getParameter("password");

        String confirmPassword =
                request.getParameter(
                        "confirmPassword"
                );

        String phone =
                request.getParameter("phone");

        String fullName =
                request.getParameter("fullName");

        String email =
                request.getParameter("email");

        try {
            authService.registerPending(
                    username,
                    password,
                    confirmPassword,
                    phone,
                    fullName,
                    email
            );

            String otp =
                    OtpGenerator_24110192.generate();

            HttpSession session =
                    request.getSession(true);

            session.setAttribute(
                    "pendingUsername",
                    username.trim().toLowerCase()
            );

            session.setAttribute(
                    "pendingEmail",
                    email.trim().toLowerCase()
            );

            session.setAttribute(
                    "pendingFullName",
                    fullName.trim()
            );

            session.setAttribute(
                    "pendingOtpHash",
                    PasswordUtil_24110192.hash(otp)
            );

            session.setAttribute(
                    "pendingOtpExpiresAt",
                    System.currentTimeMillis()
                            + OTP_VALIDITY_MILLIS
            );

            session.setAttribute(
                    "pendingOtpAttempts",
                    0
            );

            mailService.sendRegistrationOtp(
                    email.trim(),
                    fullName.trim(),
                    otp
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/verify-otp?sent=1"
            );
        } catch (IllegalArgumentException exception) {
            request.setAttribute(
                    "errorMessage",
                    exception.getMessage()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/register.jsp"
            ).forward(request, response);
        } catch (IllegalStateException exception) {
            request.setAttribute(
                    "errorMessage",
                    "Không thể gửi OTP. Kiểm tra Gmail App Password và thử lại."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/register.jsp"
            ).forward(request, response);
        }
    }

    private void verifyOtp(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            response.sendRedirect(
                    request.getContextPath()
                            + "/register"
            );

            return;
        }

        String username =
                (String) session.getAttribute(
                        "pendingUsername"
                );

        String expectedOtpHash =
                (String) session.getAttribute(
                        "pendingOtpHash"
                );

        Long expiresAt =
                (Long) session.getAttribute(
                        "pendingOtpExpiresAt"
                );

        Integer attempts =
                (Integer) session.getAttribute(
                        "pendingOtpAttempts"
                );

        if (
                username == null
                || expectedOtpHash == null
                || expiresAt == null
        ) {
            response.sendRedirect(
                    request.getContextPath()
                            + "/register"
            );

            return;
        }

        if (System.currentTimeMillis() > expiresAt) {
            clearPendingRegistration(session);

            request.setAttribute(
                    "errorMessage",
                    "Mã OTP đã hết hạn. Vui lòng đăng ký lại."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/register.jsp"
            ).forward(request, response);

            return;
        }

        int currentAttempts =
                attempts == null ? 0 : attempts;

        if (currentAttempts >= MAX_OTP_ATTEMPTS) {
            clearPendingRegistration(session);

            request.setAttribute(
                    "errorMessage",
                    "Bạn đã nhập sai OTP quá 5 lần. Vui lòng đăng ký lại."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/register.jsp"
            ).forward(request, response);

            return;
        }

        String submittedOtp =
                request.getParameter("otp");

        String submittedOtpHash =
                PasswordUtil_24110192.hash(
                        submittedOtp == null
                                ? ""
                                : submittedOtp.trim()
                );

        if (!expectedOtpHash.equals(
                submittedOtpHash
        )) {
            currentAttempts++;

            session.setAttribute(
                    "pendingOtpAttempts",
                    currentAttempts
            );

            request.setAttribute(
                    "errorMessage",
                    "Mã OTP không đúng. Còn "
                            + (
                                MAX_OTP_ATTEMPTS
                                - currentAttempts
                            )
                            + " lần thử."
            );

            prepareOtpView(session, request);

            request.getRequestDispatcher(
                    "/WEB-INF/views/verify-otp.jsp"
            ).forward(request, response);

            return;
        }

        authService.activate(username);

        clearPendingRegistration(session);

        session.setAttribute(
                "flashSuccess",
                "Kích hoạt tài khoản thành công. Bạn có thể đăng nhập."
        );

        response.sendRedirect(
                request.getContextPath()
                        + "/login"
        );
    }

    private void login(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String username =
                request.getParameter("username");

        String password =
                request.getParameter("password");

        try {
            User_24110192 user =
                    authService.authenticate(
                            username,
                            password
                    );

            HttpSession oldSession =
                    request.getSession(false);

            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession newSession =
                    request.getSession(true);

            newSession.setMaxInactiveInterval(
                    30 * 60
            );

            newSession.setAttribute(
                    "currentUser",
                    user
            );

            newSession.setAttribute(
                    "isAdmin",
                    user.isAdmin()
            );

            redirectAfterLogin(
                    user,
                    request,
                    response
            );
        } catch (IllegalArgumentException exception) {
            request.setAttribute(
                    "errorMessage",
                    exception.getMessage()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/login.jsp"
            ).forward(request, response);
        }
    }

    private void logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        HttpSession session =
                request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect(
                request.getContextPath()
                        + "/login"
        );
    }

    private void redirectAfterLogin(
            User_24110192 user,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        if (user.isAdmin()) {
            response.sendRedirect(
                    request.getContextPath()
                            + "/admin"
            );
        } else {
            response.sendRedirect(
                    request.getContextPath()
                            + "/home"
            );
        }
    }

    private void prepareOtpView(
            HttpSession session,
            HttpServletRequest request
    ) {
        String email =
                (String) session.getAttribute(
                        "pendingEmail"
                );

        request.setAttribute(
                "maskedEmail",
                maskEmail(email)
        );
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "";
        }

        int atIndex = email.indexOf('@');
        String localPart =
                email.substring(0, atIndex);

        String domain =
                email.substring(atIndex);

        if (localPart.length() <= 2) {
            return localPart.charAt(0)
                    + "***"
                    + domain;
        }

        return localPart.substring(0, 2)
                + "***"
                + domain;
    }

    private void clearPendingRegistration(
            HttpSession session
    ) {
        session.removeAttribute("pendingUsername");
        session.removeAttribute("pendingEmail");
        session.removeAttribute("pendingFullName");
        session.removeAttribute("pendingOtpHash");
        session.removeAttribute("pendingOtpExpiresAt");
        session.removeAttribute("pendingOtpAttempts");
    }
}
