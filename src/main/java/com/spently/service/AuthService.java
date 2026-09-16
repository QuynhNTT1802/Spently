package com.spently.service;

import com.spently.dto.request.auth.ChangePasswordRequest;
import com.spently.dto.request.auth.LoginRequest;
import com.spently.dto.request.auth.RefreshToken;
import com.spently.dto.request.auth.RegisterRequest;
import com.spently.dto.response.LoginResponse;
import com.spently.dto.response.ResponseResult;
import com.spently.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);

    User register(RegisterRequest registerRequest);

    ResponseEntity<ResponseResult> refreshToken(HttpServletRequest request, RefreshToken token);

    void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);

    ResponseEntity<ResponseResult> changePassword(ChangePasswordRequest changePasswordRequest);

    ResponseEntity<ResponseResult> forgotPassword(String mail);

    boolean verifyResetPassword(String username, String token);

    ResponseEntity<ResponseResult> changPasswordNotAuth(String username, ChangePasswordRequest changePasswordRequest);
}