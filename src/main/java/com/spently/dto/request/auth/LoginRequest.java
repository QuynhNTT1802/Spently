package com.spently.dto.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank
    @Schema(description = "The username or email of the user attempting to log in.",
            example = "admin")
    private String username;

    @NotBlank
    @Schema(description = "The password associated with the user account.",
            example = "123456")
    private String password;
}