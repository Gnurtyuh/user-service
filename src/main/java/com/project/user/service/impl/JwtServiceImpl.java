package com.project.user.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.project.user.dto.JwtInfo;
import com.project.user.dto.TokenPayload;
import com.project.user.dto.response.UserResponse;
import com.project.user.service.JwtService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.signerKey}")
    String signKey;
    @Override
    public TokenPayload generateRefreshToken(UserResponse userResponse){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        Date expiredTime = new Date(Instant.now().plus(14, ChronoUnit.DAYS).toEpochMilli());
        String jti = UUID.randomUUID().toString();
        String username = userResponse.getUsername();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer(username)
                .issueTime(new Date())
                .expirationTime(expiredTime)
                .claim("userId", userResponse.getUserId())
                .claim("roleName", userResponse.getRoleName())
                .claim("type", "refresh")
                .jwtID(jti)
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try{
            jwsObject.sign(new MACSigner(signKey));
            jwsObject.serialize();
            return TokenPayload.builder()
                    .token(jwsObject.serialize())
                    .jti(jti)
                    .expireTime(expiredTime)
                    .build();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String generateAccessToken(UserResponse userResponse){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userResponse.getUsername())
                .issuer(userResponse.getUsername())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(5, ChronoUnit.MINUTES).toEpochMilli()))
                .claim("userId", userResponse.getUserId())
                .claim("roleName", userResponse.getRoleName())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try{
            jwsObject.sign(new MACSigner(signKey));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
    public JwtInfo parse(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return JwtInfo.builder()
                .jti(signedJWT.getJWTClaimsSet().getJWTID())
                .issueTime(signedJWT.getJWTClaimsSet().getIssueTime())
                .expireTime(signedJWT.getJWTClaimsSet().getExpirationTime())
                .build();

    }
}
