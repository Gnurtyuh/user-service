package com.project.user.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.project.user.dto.JwtInfo;
import com.project.user.dto.TokenPayload;
import com.project.user.dto.request.AuthenticationRequest;
import com.project.user.dto.request.IntrospectRequest;
import com.project.user.dto.response.AuthenticationResponse;
import com.project.user.dto.response.IntrospectResponse;
import com.project.user.dto.response.UserResponse;
import com.project.user.entity.RedisToken;
import com.project.user.entity.Users;
import com.project.user.mapper.UserMapper;
import com.project.user.repository.RedisTokenRepository;
import com.project.user.repository.UserRepository;
import com.project.user.service.AuthenticationService;
import com.project.user.service.JwtService;
import com.project.user.service.RedisTokenService;
import com.project.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    JwtService jwtService;
    @NonFinal

    @Value("${jwt.signerKey}")
    protected String signKey;
    @Autowired
    RedisTokenService  redisTokenService;
    @Autowired
    UserService userService;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        var user = userService.findUserByEmail(request.getEmail());
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated) throw new UsernameNotFoundException("email or password incorrect");

        var token = jwtService.generateAccessToken(userMapper.toResponse(user));

        TokenPayload refreshToken = jwtService.generateRefreshToken(userMapper.toResponse(user));
        redisTokenService.createRedisToken(RedisToken.builder()
                .jwtId(refreshToken.getJti())
                .expireTime(refreshToken.getExpireTime().getTime())
                .build());

        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(refreshToken.getToken())
                .build();
    }
    public IntrospectResponse introspect(IntrospectRequest request)  {
        var token = request.getToken();
        try {
            JWSVerifier verifier = new MACVerifier(signKey.getBytes());
            SignedJWT signJwt = SignedJWT.parse(token);

            var verified = signJwt.verify(verifier);

            Date expiryTime = signJwt.getJWTClaimsSet().getExpirationTime();
            return IntrospectResponse.builder()
                    .valid(verified && expiryTime.after(new Date()))
                    .build();
        }catch (JOSEException | ParseException e  ) {
            throw new RuntimeException("JWT verification failed");
        }
    }
    @Override
    public void logout(String refreshToken) throws ParseException {
        JwtInfo jwtInfo =jwtService.parse(refreshToken);
        redisTokenService.deleteByJti(jwtInfo.getJti());
    }
    @Override
    public AuthenticationResponse refreshToken(String refreshToken) throws ParseException {
        JwtInfo jwtInfo = jwtService.parse(refreshToken);
        String jti  = jwtInfo.getJti();
        Long userId = jwtInfo.getUserId();
        RedisToken redisToken = redisTokenService.getRedisToken(jti);
        if (redisToken == null) throw new UsernameNotFoundException("Refresh token revoked or expired ");

        redisTokenService.deleteByJti(jti);

        UserResponse userResponse = userService.findUserById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken = jwtService.generateAccessToken(userResponse);
        TokenPayload refreshTokenPayload = jwtService.generateRefreshToken(userResponse);
        redisTokenService.createRedisToken(RedisToken.builder()
                .jwtId(refreshTokenPayload.getJti())
                .expireTime(refreshTokenPayload.getExpireTime().getTime())
                .build());

        return AuthenticationResponse.builder()
                .token(accessToken)
                .refreshToken(refreshTokenPayload.getToken())
                .build();
    }
}
