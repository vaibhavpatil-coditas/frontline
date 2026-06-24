package com.coditas.frontline.service;

import com.coditas.frontline.dto.request.LoginRequest;
import com.coditas.frontline.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
