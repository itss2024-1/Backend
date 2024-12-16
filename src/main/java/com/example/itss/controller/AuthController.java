package com.example.itss.controller;

import com.example.itss.domain.User;
import com.example.itss.domain.response.ResponseDto;
import com.example.itss.domain.response.user.ResUserDto;
import com.example.itss.util.error.ValidInforException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import com.example.itss.domain.request.ReqLoginDTO;
import com.example.itss.domain.response.ResLoginDto;
import com.example.itss.service.UserService;
import com.example.itss.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
        private final AuthenticationManagerBuilder authenticationManagerBuilder;
        private final SecurityUtil securityUtil;
        private final UserService userService;
        private final PasswordEncoder passwordEncoder;

        public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
                        UserService userService, PasswordEncoder passwordEncoder) {
                this.authenticationManagerBuilder = authenticationManagerBuilder;
                this.securityUtil = securityUtil;
                this.userService = userService;
                this.passwordEncoder = passwordEncoder;
        }

        @PostMapping("/auth/login")
        public ResponseEntity<ResLoginDto> login(@Valid @RequestBody ReqLoginDTO loginDto) throws ValidInforException {
                // Nạp input gồm username/password vào Security
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                loginDto.getUsername(), loginDto.getPassword());

                // xác thực người dùng => cần viết hàm loadUserByUsername
                Authentication authentication = authenticationManagerBuilder.getObject()
                                .authenticate(authenticationToken);

                // set thông tin người dùng đăng nhập vào context (có thể sử dụng sau này)
                SecurityContextHolder.getContext().setAuthentication(authentication);

                ResLoginDto res = new ResLoginDto();

                User currentUserDB = this.userService.handleGetUserByUsername(loginDto.getUsername());
                if (currentUserDB != null) {
                        ResLoginDto.UserLogin userLogin = new ResLoginDto.UserLogin(
                                        currentUserDB.getId(),
                                        currentUserDB.getEmail(),
                                        currentUserDB.getName());
                        res.setUser(userLogin);
                }

                // create access token
                String access_token = this.securityUtil.createAccessToken(authentication.getName(), res);
                res.setAccessToken(access_token);

                // create refresh token
                // String refresh_token =
                // this.securityUtil.createRefreshToken(loginDto.getUsername(), res);
                //
                // // update user
                // this.userService.updateUserToken(refresh_token, loginDto.getUsername());

                // set cookies
                // ResponseCookie resCookies = ResponseCookie
                // .from("refresh_token", refresh_token)
                // .httpOnly(true)
                // .secure(true)
                // .path("/")
                // .maxAge(refreshTokenExpiration)
                // .build();

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE)
                                .body(res);
        }

        @GetMapping("/auth/account")
        public ResponseEntity<ResLoginDto.UserGetInfores> getAccount() throws ValidInforException {
                String email = SecurityUtil.getCurrentUserLogin().isPresent()
                        ? SecurityUtil.getCurrentUserLogin().get()
                        : "";

                User currentUserDB = this.userService.handleGetUserByUsername(email);
                ResLoginDto.AccountGetInfores accountGetInfores = new ResLoginDto.AccountGetInfores();
                ResLoginDto.UserGetInfores userGetInfores = new ResLoginDto.UserGetInfores();

                if (currentUserDB != null) {
                        accountGetInfores.setId(currentUserDB.getId());
                        accountGetInfores.setEmail(currentUserDB.getEmail());
                        accountGetInfores.setName(currentUserDB.getName());
                        accountGetInfores.setAvatar(currentUserDB.getAvatar());
                        accountGetInfores.setPhone(currentUserDB.getPhone());
//                       userLogin.setRole(currentUserDB.getRole());
                        userGetInfores.setUser(accountGetInfores);
                }

                return ResponseEntity.ok().body(userGetInfores);
        }

        @PostMapping("/auth/register")
        public ResponseEntity<ResponseDto<ResUserDto>> register(@Valid @RequestBody User user) throws ValidInforException {
                ResponseDto<ResUserDto> newUser = userService.createUser(user);
                return ResponseEntity.ok().body(newUser);
        }
}
