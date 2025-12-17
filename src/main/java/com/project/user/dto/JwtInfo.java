package com.project.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class JwtInfo implements Serializable {
    String jti;
    Date expireTime;
    Date issueTime;
}
