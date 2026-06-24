package com.coditas.frontline.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
public class CustomerResponse {
    private Long id;
    private String name;
    private Instant registeredOn;
}
