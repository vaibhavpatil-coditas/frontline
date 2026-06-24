package com.coditas.frontline.dto.response;

import com.coditas.frontline.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private Instant registeredOn;
}
