package com.project.user.controller;


import com.nimbusds.jose.JOSEException;
import com.project.user.dto.JwtInfo;
import com.project.user.dto.request.AuthenticationRequest;
import com.project.user.dto.request.IntrospectRequest;
import com.project.user.dto.response.AuthenticationResponse;
import com.project.user.dto.response.IntrospectResponse;
import com.project.user.service.AuthenticationService;
import com.project.user.service.JwtService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    JwtService jwtService;
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        var result = authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/introspect")
    public ResponseEntity<IntrospectResponse> valid(@RequestBody IntrospectRequest introspectRequest) throws JOSEException {
        var result = authenticationService.introspect(introspectRequest);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestHeader(value = "Authorization", required = false) String authHeader) throws ParseException {
        String refreshToken = authHeader.substring("Bearer ".length());
        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        JwtInfo jwtInfo = jwtService.parse(refreshToken);
        if (!"refresh".equals(jwtInfo.getType())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var result = authenticationService.refreshToken(refreshToken);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) throws ParseException {
        String refreshToken = authHeader.substring("Bearer ".length());
        authenticationService.logout(refreshToken);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
