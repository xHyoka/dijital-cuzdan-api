package com.tunahan.starter.service;

import com.tunahan.starter.DTO.UserPasswordDto;
import com.tunahan.starter.DTO.UserRegisterDto;
import com.tunahan.starter.DTO.UserResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;

public interface IUserService {
    public UserResponseDto register(UserRegisterDto registerDto);
    public UserResponseDto userInformation(Long userId);
    public void password(Integer userId,UserPasswordDto userPasswordDto);
}
