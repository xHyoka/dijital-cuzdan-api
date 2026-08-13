package com.tunahan.starter.controller.UserControllerImpl;

import com.tunahan.starter.DTO.*;
import com.tunahan.starter.controller.IUserController;
import com.tunahan.starter.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(path = "/{userId}")
    public UserResponseDto userInformation(@Valid @PathVariable Long userId){
        return userService.userInformation(userId);
    }

    @PostMapping(path = "/password/{userId}")
    public ResponseEntity<ApiResponseDto> password(@PathVariable Integer userId ,@Valid @RequestBody UserPasswordDto passwordDto){
        userService.password(userId,passwordDto);
        ApiResponseDto response = new ApiResponseDto();
        response.setMessage("şifre başarıyla güncellendi");
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }

}
