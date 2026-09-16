package com.spently.dto.request.user;


import com.spently.config.Constant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    @Schema(example = "Nguyen Van A")
    @NotBlank(message = "{" + Constant.ERROR_REQUIRED_NAME + "}")
    String fullName;

    @Schema(example = "email@gmail.com")
    @Email(message = "{" + Constant.ERROR_INVALID_EMAIL + "}")
    @Pattern(
            regexp = "^(?!.*\\.\\.)[A-Za-z0-9]+([._%+-]?[A-Za-z0-9]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "{" + Constant.ERROR_INVALID_EMAIL + "}"
    )
    String email;

    @Schema(example = "admin")
    @NotNull
    String username;

    @Schema(example = "0987654321")
    String phone;

    @Schema(example = "Company id2")
    String companyId;

    @Schema(example = Constant.ROLE_USER)
    @NotNull
    String role;

    @Schema(example = "1", description = "status user with 1 = active and 0 = inactive")
    @NotNull
    int status;
}