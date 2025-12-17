package com.project.user.service;

import com.project.user.dto.response.UserResponse;
import com.project.user.entity.RedisToken;

public interface RedisTokenService {
    void createRedisToken(RedisToken redisToken);
    RedisToken getRedisToken(String jti);
    void deleteByJti(String jti);
}
