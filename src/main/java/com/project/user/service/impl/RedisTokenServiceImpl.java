package com.project.user.service.impl;

import com.project.user.dto.response.UserResponse;
import com.project.user.entity.RedisToken;
import com.project.user.repository.RedisTokenRepository;
import com.project.user.service.RedisTokenService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RedisTokenServiceImpl implements RedisTokenService {
    @Autowired
    RedisTokenRepository redisTokenRepository;
    @Override
    public void createRedisToken(RedisToken redisToken) {
        redisTokenRepository.save(redisToken);
    }

    @Override
    public RedisToken getRedisToken(String jti) {
        return redisTokenRepository.findById(jti).orElseThrow(() -> new UsernameNotFoundException("jti not found"));
    }

    @Override
    public void deleteByJti(String jti) {
        RedisToken redisToken = getRedisToken(jti);
        redisTokenRepository.delete(redisToken);
    }
}
