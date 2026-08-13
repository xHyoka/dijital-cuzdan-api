package com.tunahan.starter.controller;

import com.tunahan.starter.DTO.ApiResponseDto;
import com.tunahan.starter.DTO.UserPasswordDto;
import com.tunahan.starter.DTO.UserRegisterDto;
import com.tunahan.starter.DTO.UserResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface IUserController {
    public UserResponseDto register(@Valid @RequestBody UserRegisterDto registerDto);
    public UserResponseDto userInformation(@Valid @PathVariable Long userId);
    public ResponseEntity<ApiResponseDto> password(@PathVariable Integer userId , @Valid @RequestBody UserPasswordDto passwordDto);
}
