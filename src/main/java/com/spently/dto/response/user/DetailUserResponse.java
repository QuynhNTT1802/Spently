package com.spently.dto.response.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DetailUserResponse {
    String userId;
    String email;
    String role;
    String fullName;
    String phone;
    String avatarUrl;
    Integer status;
    String companyId;
    String companyName;
    LocalDateTime lastActive;
    LocalDateTime  createdAt;
}