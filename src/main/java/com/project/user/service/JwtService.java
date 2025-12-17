package com.project.user.service;

import com.project.user.dto.TokenPayload;
import com.project.user.dto.response.UserResponse;

public interface JwtService {
    String generateAccessToken(UserResponse userResponse);
    TokenPayload generateRefreshToken(UserResponse userResponse);

}
