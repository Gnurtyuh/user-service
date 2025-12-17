package com.project.user.service;

import com.project.user.dto.JwtInfo;
import com.project.user.dto.TokenPayload;
import com.project.user.dto.response.UserResponse;

import java.text.ParseException;

public interface JwtService {
    String generateAccessToken(UserResponse userResponse);
    TokenPayload generateRefreshToken(UserResponse userResponse);
    JwtInfo parse(String token) throws ParseException;
}
