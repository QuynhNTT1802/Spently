package com.spently.service.impl;

import com.spently.config.Constant;
import com.spently.config.CustomUserDetails;
import com.spently.config.JwtUtils;
import com.spently.dto.projection.UserProjection;
import com.spently.dto.request.auth.ChangePasswordRequest;
import com.spently.dto.request.auth.LoginRequest;
import com.spently.dto.request.auth.RefreshToken;
import com.spently.dto.request.auth.RegisterRequest;
import com.spently.dto.response.LoginResponse;
import com.spently.dto.response.ResponseResult;
import com.spently.dto.response.auth.RefreshTokenResponse;
import com.spently.entity.PasswordResetToken;
import com.spently.entity.User;
import com.spently.entity.UserToken;
import com.spently.exception.NotFoundException;
import com.spently.exception.UnauthorizedException;
import com.spently.mapper.UserMapper;
import com.spently.repository.PasswordResetTokenRepository;
import com.spently.repository.UserRepository;
import com.spently.repository.UserTokenRepository;
import com.spently.service.AuthService;
import com.spently.service.MailService;
import com.spently.utils.GenerateCodeUtils;
import com.spently.utils.MessageUtil;
import com.spently.utils.SecurityUserDetailsUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserTokenRepository userTokenRepository;
    private final UserDetailsServiceImpl customUserDetailsService;
    private final MessageUtil messageUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final MailService mailService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            if (userDetails.getStatus() == 0) {
                throw new IllegalArgumentException(Constant.ERROR_ACCOUNT_LOCKED);
            }

            User u = userRepository.findById(userDetails.getId()).orElseThrow(() -> {
                log.error("User not found with id {} ", userDetails.getId());
                log.error("userDetails " + userDetails);
                return new NotFoundException(Constant.ERROR_USER_NOT_FOUND);
            });

            UserProjection userProjection = userRepository.findUserById(userDetails.getId()).orElseThrow(() -> {
                log.error("User not found with id {} ", userDetails.getId());
                log.error("userDetails " + userDetails);
                return new NotFoundException(Constant.ERROR_USER_NOT_FOUND);
            });

            userRepository.save(u);

            String accessToken = jwtUtils.generateToken(userDetails, false);
            String refreshToken = jwtUtils.generateToken(userDetails, true);

            // Get refresh token's expiry time
            LocalDateTime ex = jwtUtils.extractExpiredAt(refreshToken);

            userTokenRepository.save(
                    UserToken.builder()
                            .username(userDetails.getUsername())
                            .token(refreshToken)
                            .expiredAt(ex)
                            .build()
            );

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .username(userDetails.getUsername())
                    .fullName(u.getFullName())
                    .avatarUrl(u.getAvatarUrl())
                    .phone(u.getPhone())
                    .role(userDetails.getRole())
                    .build();

        } catch (AuthenticationException ex) {
            throw new UnauthorizedException(Constant.ERROR_AUTH_INVALID_CREDENTIALS);
        }
    }

    @Override
    public User register(RegisterRequest request) {
        Optional<String> uId = userRepository.findUserIdByEmail(request.getEmail());

        if (uId.isPresent()) {
            log.warn("Email already existing by id {}", uId.get());
            throw new IllegalArgumentException(Constant.ERROR_EXITED_EMAIL);
        }
        if (!request.getPassword().equals(request.getRepeatPassword()))
            throw new IllegalArgumentException(Constant.ERROR_MATCH_PASSWORD);
        User user = UserMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseResult> refreshToken(HttpServletRequest request, RefreshToken reToken) {
        String token = reToken.getToken();

        if (StringUtils.hasText(token) && jwtUtils.validateToken(token)
                && jwtUtils.getTokenTypeFromJwt(token).equals(Constant.TYPE_REFRESH)
                && userTokenRepository.existsByToken(token)) {
            String username = jwtUtils.extractUsername(token);
            CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
            String accessToken = jwtUtils.generateToken(userDetails, Boolean.FALSE);
            String refreshToken = jwtUtils.generateToken(userDetails, Boolean.TRUE);
            userTokenRepository.deleteByToken(token);
            userTokenRepository.save(UserToken.builder()
                    .token(refreshToken)
                    .username(userDetails.getUsername())
                    .build());
            return ResponseResult.ok(RefreshTokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build());
        }
        return ResponseResult.unauthorized(messageUtil.getMessage(Constant.ERROR_AUTH_INVALID_REFRESH_TOKEN));
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String refreshToken = request.getHeader("Refresh-Token");
        if (StringUtils.hasText(refreshToken)) {
            userTokenRepository.deleteByToken(refreshToken);
        }
        SecurityContextLogoutHandler logout = new SecurityContextLogoutHandler();
        logout.logout(request, response, authentication);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseResult> changePassword(ChangePasswordRequest request) {
        CustomUserDetails user = (CustomUserDetails) SecurityUserDetailsUtils.getAuthenticatedUser();
        String id = user.getId();

        User employee = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constant.ERROR_USER_NOT_FOUND));

        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();
        String confirmPassword = request.getConfirmPassword();

        boolean matchPassword = passwordEncoder.matches(oldPassword, employee.getPassword());
        if (!matchPassword) {
            throw new IllegalArgumentException(Constant.ERROR_PASSWORD_NOT_MATCH);
        }
        if (oldPassword.equals(newPassword)) {
            throw new IllegalArgumentException(Constant.ERROR_NEW_PASSWORD_SAME_AS_OLD);
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException(Constant.ERROR_CONFIRM_PASSWORD_NOT_MATCH);
        }

        employee.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(employee);

        return ResponseResult.ok(messageUtil.getMessage(Constant.SUC_PASSWORD_UPDATED), null);
    }

    @Override
    public ResponseEntity<ResponseResult> forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Constant.ERROR_USER_NOT_FOUND));

        String rawNewPassword = GenerateCodeUtils.randomPassword(6);
        String encodePassword = passwordEncoder.encode(rawNewPassword);
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = passwordResetTokenRepository.findByUsername(user.getUsername());

        if (resetToken != null) {

            resetToken.setToken(token);
            resetToken.setNewPassword(encodePassword);

            passwordResetTokenRepository.save(resetToken);

        } else {
            PasswordResetToken passwordResetToken = new PasswordResetToken();
            PasswordResetToken.builder().newPassword(encodePassword).token(token).username(user.getUsername()).build();
            passwordResetToken.setNewPassword(encodePassword);
            passwordResetToken.setToken(token);
            passwordResetToken.setUsername(user.getUsername());

            passwordResetTokenRepository.save(passwordResetToken);
        }

        mailService.sendForgotPasswordMail(user.getEmail(), user.getUsername(), rawNewPassword, token);

        return ResponseResult.ok(messageUtil.getMessage(Constant.SUCCESS_EMAIL_SENT), null);
    }

    @Override
    @Transactional
    public boolean verifyResetPassword(String username, String token) {
        boolean check = true;
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUsernameAndToken(username, token)
                .orElse(null);
        if (passwordResetToken == null) {
            return false;
        }

        User user = userRepository.findByUsername(username)
                .orElse(null);
        if (user == null) {
            return false;
        } else {
            user.setPassword(passwordResetToken.getNewPassword());
            userRepository.save(user);
            passwordResetTokenRepository.delete(passwordResetToken);
        }

        return check;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseResult> changPasswordNotAuth(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(Constant.ERROR_USER_NOT_FOUND));

        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();
        String confirmPassword = request.getConfirmPassword();

        boolean matchPassword = passwordEncoder.matches(oldPassword, user.getPassword());
        if (!matchPassword) {
            throw new IllegalArgumentException(Constant.ERROR_PASSWORD_NOT_MATCH);
        }
        if (oldPassword.equals(newPassword)) {
            throw new IllegalArgumentException(Constant.ERROR_NEW_PASSWORD_SAME_AS_OLD);
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException(Constant.ERROR_CONFIRM_PASSWORD_NOT_MATCH);
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        return ResponseResult.ok(messageUtil.getMessage(Constant.SUCCESS_UPDATE_SUCCESS), null);
    }
}
