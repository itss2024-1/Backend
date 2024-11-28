package com.example.itss.domain.dto.request.user;

import com.example.itss.util.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ReqUserDto {
    private Integer id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
}
