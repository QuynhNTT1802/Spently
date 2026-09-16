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
public class ListUserResponse {
    String userId;
    String email;
    String username;
    String role;
    String fullName;
    String phone;
    String avatarUrl;
    Integer status;
    LocalDateTime  createdAt;
}
