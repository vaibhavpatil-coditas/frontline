package com.coditas.frontline.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RatingsRequest {
    @Size(min = 0, max = 5, message = "Ratings should be between 0 and 5")
    private Integer ratings;
}
