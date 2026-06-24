package com.coditas.frontline.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
public class TeamResponse {
    private Long id;
    private Long managerId;
    private Long teamSize;
    private Instant createdAt;
}
