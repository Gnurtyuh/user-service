package com.project.user.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.project.user.dto.request.AuthenticationRequest;
import com.project.user.dto.request.IntrospectRequest;
import com.project.user.dto.response.AuthenticationResponse;
import com.project.user.dto.response.IntrospectResponse;
import com.project.user.dto.response.UserResponse;
import com.project.user.entity.Users;
import com.project.user.mapper.UserMapper;
import com.project.user.repository.UserRepository;
import com.project.user.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;
    @NonFinal

    @Value("${jwt.signerKey}")
    protected String signKey;
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        var user = userRepository.findUserByEmail(request.getEmail());
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated) throw new UsernameNotFoundException("email or password incorrect");

        var token = generateToken(userMapper.toResponse(user));

        return AuthenticationResponse.builder()
                .token(token)
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
    String generateToken(UserResponse userResponse){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userResponse.getUsername())
                .issuer(userResponse.getUsername())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("userId", userResponse.getUserId())
                .claim("roleId", userResponse.getUserId())
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
}
