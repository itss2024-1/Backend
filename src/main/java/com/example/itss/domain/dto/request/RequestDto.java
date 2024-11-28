package com.example.itss.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto<T> {
    private int status; // HTTP status code (e.g., 200, 400, 500)
    private String message; // Message to describe the response
    private T data; // Generic type for the actual response data
}
