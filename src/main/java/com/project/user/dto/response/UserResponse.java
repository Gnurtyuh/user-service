package com.project.user.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long userId;
    String username;
    String email;
    String fullName;
    String roleName;
    Timestamp createdDate;
    Timestamp updatedDate;
}
