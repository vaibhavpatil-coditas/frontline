package com.coditas.frontline.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserRequest {
    @Email(message = "Incorrect email structure, please correct the email")
    @NotBlank(message = "Email cannot be blank, please enter a email")
    private String email;
    @NotBlank(message = "Password cannot be blank, please enter a password")
    private String password;
    @NotBlank(message = "Name cannot be blank, please enter a name")
    private String name;
}
