package com.coditas.frontline.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PriorityRequest {
    @Size(min = 0, max=10, message = "Priority should be between 0 and 10")
    private Integer priority;
}
