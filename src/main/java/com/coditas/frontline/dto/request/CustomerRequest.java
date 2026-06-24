package com.coditas.frontline.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerRequest {
    private UserRequest user;
    private String name;
}
