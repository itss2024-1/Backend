package com.example.itss.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {
    private int status; // HTTP status code (e.g., 200, 400, 500)
    private Object message; // Message to describe the response
    private T data; // Generic type for the actual response data
}
