package com.tunahan.starter.controller.UserControllerImpl;

import com.tunahan.starter.DTO.AccountResponseDto;
import com.tunahan.starter.DTO.DepositRequestDto;
import com.tunahan.starter.DTO.UserRegisterDto;
import com.tunahan.starter.DTO.UserResponseDto;
import com.tunahan.starter.controller.IUserController;
import com.tunahan.starter.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user/api")
public class UserControllerImpl implements IUserController {

    @Autowired
    private IUserService userService;

    @PostMapping(path = "/register")
    public UserResponseDto register(@Valid @RequestBody UserRegisterDto registerDto) {
        return userService.register(registerDto);
    }
}
