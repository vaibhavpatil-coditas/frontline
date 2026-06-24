package com.coditas.frontline.controller;

import com.coditas.frontline.constants.ApiPaths;
import com.coditas.frontline.dto.request.UserRequest;
import com.coditas.frontline.dto.response.ApplicationResponse;
import com.coditas.frontline.dto.response.UserResponse;
import com.coditas.frontline.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(ApiPaths.BASE_PATH)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(ApiPaths.Customer.CUSTOMERS)
    public ResponseEntity<ApplicationResponse<UserResponse>> registerCustomer(@Valid @RequestBody UserRequest request){
        UserResponse response = userService.registerCustomer(request);
        URI location = URI.create(ApiPaths.BASE_PATH+ApiPaths.Customer.CUSTOMERS+"/"+response.getId());
        return ResponseEntity.created(location).body(ApplicationResponse.success(response, "Customer registered successfully"));
    }

    @PostMapping(ApiPaths.Manager.MANAGERS)
    public ResponseEntity<ApplicationResponse<UserResponse>> registerManager(@Valid @RequestBody UserRequest request){
        UserResponse response = userService.registerManager(request);
        URI location = URI.create(ApiPaths.BASE_PATH+ApiPaths.Manager.MANAGERS+"/"+response.getId());
        return ResponseEntity.created(location).body(ApplicationResponse.success(response, "Manager registered successfully"));
    }

    @PostMapping(ApiPaths.Agent.AGENTS)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ApplicationResponse<UserResponse>> registerAgent(@Valid @RequestBody UserRequest request){
        UserResponse response = userService.registerAgent(request);
        URI location = URI.create(ApiPaths.BASE_PATH+ApiPaths.Agent.AGENTS+"/"+response.getId());
        return ResponseEntity.created(location).body(ApplicationResponse.success(response, "Agent registered successfully"));
    }
}
