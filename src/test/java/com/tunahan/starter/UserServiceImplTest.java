package com.tunahan.starter;


import com.tunahan.starter.DTO.UserPasswordDto;
import com.tunahan.starter.DTO.UserRegisterDto;
import com.tunahan.starter.DTO.UserResponseDto;
import com.tunahan.starter.exception.AccountNotFoundException;
import com.tunahan.starter.exception.WrongPasswordException;
import com.tunahan.starter.model.User;
import com.tunahan.starter.repository.UserRepository;
import com.tunahan.starter.service.ServiceImpl.UserServiceImpl;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)

public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("register: Kullanıcıyı kayıt etmeli ve ")
    void register_ShouldRegisterUser(){

        UserRegisterDto dto = new UserRegisterDto();
        dto.setUsername("1234");
        dto.setPassword("1234");

        User user = new User();
        user.setUsername("tunahan");

        when(passwordEncoder.encode("1234")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);


        UserResponseDto result = userService.register(dto);

        assertNotNull(result);
        assertEquals("tunahan",result.getUsername());
        verify(passwordEncoder, times(1)).encode("1234");
    }

    @Test
    @DisplayName("userInformation : Kullanıcı bilgilerini geri döndürmeli")
    void userInformation_ShouldReturnUserInfo(){
        User user = new User();
        user.setId(1);
        user.setUsername("tunahan");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserResponseDto result = userService.userInformation(1L);

        assertNotNull(result);
        assertEquals(result.getUsername(),"tunahan");


    }

    @Test
    @DisplayName("userInformation: Kullanıcı bulunamayınca hata fırlatmalı")
    void userInformation_ShouldThrowExceptionWhenUserNotFound(){
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> userService.userInformation(99L));
    }

    @Test
    @DisplayName("password: Kullanıcı şifresini değiştirmeli")
    void password_ShouldChangePasswordIfOldPasswordTrue(){
        User user = new User();
        user.setPassword("hashedOldPassword");
        user.setId(1);

        UserPasswordDto userPasswordDto = new UserPasswordDto();
        userPasswordDto.setOldPassword("1234");
        userPasswordDto.setNewPassword("5678");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("1234","hashedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode("5678")).thenReturn("hashedNewPassword");

        userService.password(1,userPasswordDto);
        verify(userRepository,times(1)).save(any(User.class));

    }



    @Test
    @DisplayName("password: Kullanıcı şifresi yanlışsa değiştirmemeli")
    void password_ShouldntChangePasswordIfOldPasswordFalse(){
        User user = new User();
        user.setPassword("hashedOldPassword");
        user.setId(1);

        UserPasswordDto userPasswordDto = new UserPasswordDto();
        userPasswordDto.setOldPassword("1234");
        userPasswordDto.setNewPassword("5678");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("1234","hashedOldPassword")).thenReturn(false);

        assertThrows(WrongPasswordException.class,
                () -> userService.password(1, userPasswordDto));

    }

    @Test
    @DisplayName("password: Hesap bulunamazsa hata fırlatmalı")
    void password_ShouldThrowAccountNotFoundException(){
    when(userRepository.findById(99)).thenReturn(Optional.empty());

        UserPasswordDto userPasswordDto = new UserPasswordDto();
        userPasswordDto.setOldPassword("1234");
        userPasswordDto.setNewPassword("5678");

    assertThrows(AccountNotFoundException.class,
            () -> userService.password(99,userPasswordDto));
    }

}
