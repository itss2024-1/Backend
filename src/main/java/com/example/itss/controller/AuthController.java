package com.example.itss.controller;

import com.example.itss.domain.dto.request.auth.ReqLoginDto;
import com.example.itss.domain.dto.response.auth.ResLoginDto;
import com.example.itss.domain.model.User;
import com.example.itss.service.UserService;
import com.example.itss.util.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Value("${security.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpriration;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtils securityUtils;
    private final UserService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtils securityUtils,
                          UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtils = securityUtils;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody ReqLoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword());
        // xác thực nguowif dùng => vieest hàm tạo loaduserbyusername
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // create access_token and refresh token

        ResLoginDto res = new ResLoginDto();
        User currentUser = this.userService.handleGetUserByEmail(loginDto.getUsername());
        ResLoginDto.UserLogin userLogin = new ResLoginDto.UserLogin(
                currentUser.getId(),
                currentUser.getName(),
                currentUser.getEmail());

        res.setUserLogin(userLogin);

        String access_token = this.securityUtils.createAccessToken(currentUser.getEmail(), res);
        res.setAccessToken(access_token);

//        String refreshToken = this.securityUtils.createRefreshToken(loginDto.getUsername(), res);
//        this.userService.handleUpdateResfreshToken(currentUser.getEmail(), refreshToken);

        // create cookie
//        ResponseCookie springCookie = ResponseCookie.from("refresh-token", refreshToken)
//                .httpOnly(true)
//                .secure(true)
//                .path("/")
//                .maxAge(refreshTokenExpriration)
//                .build();

        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE)
                .body(res);
    }

    @GetMapping("/account")
    public ResponseEntity<ResLoginDto.UserGetAccount> getAccount() {
        String email = SecurityUtils.getCurrentUserLogin().isPresent()
                ? SecurityUtils.getCurrentUserLogin().get()
                : "";

        User user = this.userService.handleGetUserByEmail(email);
        ResLoginDto.UserLogin userLogin = new ResLoginDto.UserLogin();
        ResLoginDto.UserGetAccount userAccount = new ResLoginDto.UserGetAccount();
        if (user != null) {
            userLogin.setId(user.getId());
            userLogin.setEmail(user.getEmail());
            userLogin.setName(user.getName());
            userAccount.setUser(userLogin);
        }
        return ResponseEntity.ok().body(userAccount);
    }
}
