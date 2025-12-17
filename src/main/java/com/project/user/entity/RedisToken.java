package com.project.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("RedisHash")
@Builder
public class RedisToken {
    @Id
    private String jwtId;

    @TimeToLive(unit = TimeUnit.DAYS)
    private Long expireTime;
}
