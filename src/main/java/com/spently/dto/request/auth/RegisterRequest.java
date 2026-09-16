package com.spently.dto.request.auth;


import com.spently.config.Constant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @Schema(example = "Nguyen Van A")
    @NotBlank(message = "{" + Constant.ERROR_REQUIRED_NAME + "}")
    private String name;

    @Schema(example = "Company id")
    private String companyId;

    @Schema(example = "email@gmail.com")
    @Email(message = "{" + Constant.ERROR_INVALID_EMAIL + "}")
    @Pattern(
            regexp = "^(?!.*\\.\\.)[A-Za-z0-9]+([._%+-]?[A-Za-z0-9]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "{" + Constant.ERROR_INVALID_EMAIL + "}"
    )
    private String email;

    @NotNull
    private String username;

    @Schema(example = "0987654321")
    private String phone;

    @Schema(example = "123aA@123")
    @NotBlank(message = "{" + Constant.ERROR_REQUIRED_PASSWORD + "}")
    @Size(min = 6, message = "{" + Constant.ERROR_MIN_PASSWORD + "}")
    private String password;

    @Schema(example = "123aA@123")
    @NotBlank(message = "{" + Constant.ERROR_REQUIRED_RE_PASSWORD + "}")
    private String repeatPassword;
}
