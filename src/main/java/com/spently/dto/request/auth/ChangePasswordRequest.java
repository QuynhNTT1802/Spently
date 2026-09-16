package com.spently.dto.request.auth;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ChangePasswordRequest {
    @NotNull
    String oldPassword;

    @NotNull
    String newPassword;

    @NotNull
    String confirmPassword;
}