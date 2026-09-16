package com.spently.dto.projection;

import java.time.LocalDateTime;

public interface UserProjection {
    String getUserId();
    String getUsername();
    String getEmail();
    String getRole();
    String getFullName();
    String getPhone();
    String getAvatarUrl();
    Integer getStatus();
    LocalDateTime  getCreatedAt();
}
