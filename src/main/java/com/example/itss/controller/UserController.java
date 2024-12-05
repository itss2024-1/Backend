package com.example.itss.controller;

import com.example.itss.domain.User;
import com.example.itss.domain.response.ResponseDto;
import com.example.itss.domain.response.ResultPaginationDto;
import com.example.itss.domain.response.user.ResUserDto;
import com.example.itss.service.UserService;
import com.example.itss.util.error.ValidInforException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto<ResUserDto>> createUse(@Valid @RequestBody User user) throws ValidInforException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<ResultPaginationDto>> getUserPagination(
            @Filter Specification<User> spec,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUsersPagination(spec, pageable));
    }

    @PutMapping
    public ResponseEntity<ResponseDto<ResUserDto>> updateUser(@RequestBody User user) throws ValidInforException {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.updateUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> deleteUser(@PathVariable Long id) throws ValidInforException {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(this.userService.deleteUser(id));
    }
}
