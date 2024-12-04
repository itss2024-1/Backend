package com.example.itss.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @NotBlank(message = "email không được để trống")
    private String email;

    @NotBlank(message = "password không được để trống")
    private String password;

    private int age;
    private String address;
    private String phone;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

//    @PrePersist
//    public void handleBeforeCreate() {
//        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
//                ? SecurityUtil.getCurrentUserLogin().get()
//                : "";
//
//        this.createdAt = Instant.now();
//    }
//
//    @PreUpdate
//    public void handleBeforeUpdate() {
//        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
//                ? SecurityUtil.getCurrentUserLogin().get()
//                : "";
//
//        this.updatedAt = Instant.now();
//    }
}
