package com.example.itss.domain.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResUserDto {
    private long id;
    private String name;
    private String email;
    private int age;
    private String address;
    private String phone;
    private String avatar;
    private Instant createdAt;
    private Instant updatedAt;
}
