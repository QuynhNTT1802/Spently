package com.spently.dto.request.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateProfile {
    String fullName;
    String email;
    String phone;
    MultipartFile profileImage;
    String companyId;
}
