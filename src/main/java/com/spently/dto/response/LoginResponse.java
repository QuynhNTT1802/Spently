package com.spently.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String username;
    private String fullName;
    private String phone;
    private String avatarUrl;
    private String accessToken;
    private String refreshToken;
    private String role;
}
