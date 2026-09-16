package com.spently.mapper;

import com.spently.config.Constant;
import com.spently.dto.projection.UserProjection;
import com.spently.dto.request.auth.RegisterRequest;
import com.spently.dto.request.user.UserCreateRequest;
import com.spently.dto.request.user.UserUpdateProfile;
import com.spently.dto.request.user.UserUpdateRequest;
import com.spently.dto.response.user.DetailUserResponse;
import com.spently.dto.response.user.ListUserResponse;
import com.spently.entity.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserMapper {
    public static User toUser(RegisterRequest request) {
        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFullName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(Constant.ROLE_USER);
        user.setStatus(1);

        return user;
    }

    public static User toUser(UserCreateRequest request) {
        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setStatus(request.getStatus());

        return user;
    }

    public static void toUser(User user, UserUpdateProfile request) {
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
    }

    public static void toUser(User user, UserUpdateRequest request) {
        user.setFullName(request.getFullName());
        user.setRole(request.getRole().toUpperCase());
        user.setStatus(request.getStatus());
    }

    public static DetailUserResponse toUserDetailResponse(UserProjection userProjection) {
        return DetailUserResponse.builder()
                .userId(userProjection.getUserId())
                .email(userProjection.getEmail())
                .role(userProjection.getRole())
                .fullName(userProjection.getFullName())
                .phone(userProjection.getPhone())
                .avatarUrl(userProjection.getAvatarUrl())
                .status(userProjection.getStatus())
                .createdAt(userProjection.getCreatedAt())
                .build();
    }

    public static ListUserResponse toUserResponse(UserProjection userProjection) {
        return ListUserResponse.builder()
                .userId(userProjection.getUserId())
                .username((userProjection.getUsername()))
                .email(userProjection.getEmail())
                .role(userProjection.getRole())
                .fullName(userProjection.getFullName())
                .phone(userProjection.getPhone())
                .avatarUrl(userProjection.getAvatarUrl())
                .status(userProjection.getStatus())
                .createdAt(userProjection.getCreatedAt())
                .build();
    }
}
