package com.tunahan.starter.service;

import com.tunahan.starter.DTO.UserRegisterDto;
import com.tunahan.starter.DTO.UserResponseDto;

public interface IUserService {
    public UserResponseDto register(UserRegisterDto registerDto);
}
