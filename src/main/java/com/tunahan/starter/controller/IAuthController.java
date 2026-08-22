package com.tunahan.starter.controller;

import com.tunahan.starter.DTO.LoginRequest;
import com.tunahan.starter.DTO.LoginResponse;
import com.tunahan.starter.DTO.UserRegisterDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface IAuthController {
    public ResponseEntity<String> register (@RequestBody UserRegisterDto userRegisterDto);
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request);
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader);

}
