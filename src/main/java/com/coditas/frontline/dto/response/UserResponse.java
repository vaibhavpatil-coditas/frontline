package com.coditas.frontline.dto.response;

import com.coditas.frontline.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponse {
    private Long id;
    private String email;
    private Role role;
}
