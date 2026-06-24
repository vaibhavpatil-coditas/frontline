package com.coditas.frontline.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Builder
@Getter @Setter
public class ErrorResponse {
    private String message;
    private Instant timestamp;
    private Integer status;
}
