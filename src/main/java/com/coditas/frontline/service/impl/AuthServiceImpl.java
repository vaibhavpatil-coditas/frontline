package com.coditas.frontline.service.impl;

import com.coditas.frontline.dto.request.LoginRequest;
import com.coditas.frontline.dto.response.LoginResponse;
import com.coditas.frontline.entity.User;
import com.coditas.frontline.security.jwt.JwtUtil;
import com.coditas.frontline.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User principal = (User) authentication.getPrincipal();
        assert principal != null;
        log.info("{} successfully logged in", request.getEmail());
        return new LoginResponse(jwtUtil.generateToken(principal));
    }
}
