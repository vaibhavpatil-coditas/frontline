package com.coditas.frontline.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private Object errors;

    public static <T> ApplicationResponse<T> success(T data, String message) {
        return ApplicationResponse.<T>builder()
                .message(message)
                .data(data)
                .success(true)
                .build();
    }
}
