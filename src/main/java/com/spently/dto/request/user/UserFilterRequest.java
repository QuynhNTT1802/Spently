package com.spently.dto.request.user;

import com.spently.dto.request.PaginationRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFilterRequest extends PaginationRequest {
    String keyword;
    String role;
    Integer status;
}
