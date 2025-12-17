package com.project.user.service;

import com.nimbusds.jose.JOSEException;
import com.project.user.dto.request.AuthenticationRequest;
import com.project.user.dto.request.IntrospectRequest;
import com.project.user.dto.response.AuthenticationResponse;
import com.project.user.dto.response.IntrospectResponse;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException;
    AuthenticationResponse refreshToken(String refreshToken) throws ParseException;

    void logout(String refreshToken) throws ParseException;
}
