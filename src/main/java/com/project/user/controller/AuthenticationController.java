package com.project.user.controller;


import com.nimbusds.jose.JOSEException;
import com.project.user.dto.request.AuthenticationRequest;
import com.project.user.dto.request.IntrospectRequest;
import com.project.user.dto.response.AuthenticationResponse;
import com.project.user.dto.response.IntrospectResponse;
import com.project.user.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;
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
}
