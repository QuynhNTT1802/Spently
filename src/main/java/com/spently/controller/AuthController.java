package com.spently.controller;

import com.spently.config.Constant;
import com.spently.dto.request.auth.ChangePasswordRequest;
import com.spently.dto.request.auth.LoginRequest;
import com.spently.dto.request.auth.RefreshToken;
import com.spently.dto.request.auth.RegisterRequest;
import com.spently.dto.response.ResponseResult;
import com.spently.service.AuthService;
import com.spently.utils.MessageUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MessageUtil messageUtil;

    @Operation(
            summary = "register",
            description = "Register new account for user."
    )
    @PostMapping("/register")
    public ResponseEntity<ResponseResult> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseResult.created(Constant.SUCCESS_CREATE_SUCCESS, authService.register(request));
    }

    @Operation(
            summary = "Login",
            description = "Check authentication to login and generate token for user"
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseResult.ok(null, authService.login(loginRequest));
    }

    @Operation(
            summary = "Refresh Token",
            description = "Generate new access token using refresh token"
    )
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, @RequestBody RefreshToken token) {
        return authService.refreshToken(request, token);
    }

    @Operation(
            summary = "Logout",
            description = "Logout and invalidate the user session"
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, @RequestHeader(name = "Refresh-Token") String header, Authentication authentication) {
        authService.logout(request, response, authentication);
        return ResponseResult.ok("Logout successfully", "");
    }

    @Operation(summary = "Change password", description = "Change password")
    @PostMapping("/change-password")
    public ResponseEntity<ResponseResult> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        return authService.changePassword(request);
    }

    @Operation(summary = "Forgot password and send to mail", description = "Forgot password and send to mail")
    @PostMapping("/forget-password")
    public ResponseEntity<ResponseResult> forgetPassword(@RequestParam String mail) {
        return authService.forgotPassword(mail);
    }

    @GetMapping("/verify-reset-password")
    public ResponseEntity<ResponseResult> verifyResetPassword(@RequestParam String username, @RequestParam String token) {

        boolean isValid = authService.verifyResetPassword(username, token);

        if (!isValid) {
            return ResponseResult.badRequest(messageUtil.getMessage(Constant.ERROR_VERIFY_FAILED));
        }

        return ResponseResult.ok(messageUtil.getMessage(Constant.SUCCESS_PASSWORD_RESET_VERIFIED), null);
    }

    @Operation(summary = "Change password not login", description = "Change password not login")
    @PostMapping("forgot-password/reset")
    public ResponseEntity<ResponseResult> forgotPassword(@RequestParam String username, @ParameterObject ChangePasswordRequest request) {
        return authService.changPasswordNotAuth(username, request);
    }


}
