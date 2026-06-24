package com.coditas.frontline.service.impl;

import com.coditas.frontline.Security.jwt.JwtUtil;
import com.coditas.frontline.dto.request.LoginRequest;
import com.coditas.frontline.dto.response.LoginResponse;
import com.coditas.frontline.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return new LoginResponse(jwtUtil.generateToken(principal.getUsername()));
    }
}
