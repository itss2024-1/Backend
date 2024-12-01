package com.example.itss.domain.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqLoginDto {
    @NotBlank(message = "username not null")
    private String username;

    @NotBlank(message = "password not null")
    private String password;
}