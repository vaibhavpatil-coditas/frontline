package com.coditas.frontline.service;

import com.coditas.frontline.dto.request.UserRequest;
import com.coditas.frontline.dto.response.UserResponse;
import jakarta.validation.Valid;

public interface UserService {

    UserResponse registerManager(@Valid UserRequest request);

    UserResponse registerAgent(@Valid UserRequest request);

    UserResponse registerCustomer(@Valid UserRequest request);
}
