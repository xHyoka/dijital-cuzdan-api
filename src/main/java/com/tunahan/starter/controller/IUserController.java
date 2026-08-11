package com.tunahan.starter.controller;

import com.tunahan.starter.DTO.UserRegisterDto;
import com.tunahan.starter.DTO.UserResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

public interface IUserController {
    public UserResponseDto register(@Valid @RequestBody UserRegisterDto registerDto);
}
