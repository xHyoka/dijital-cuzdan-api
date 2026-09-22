package com.tunahan.starter.service.ServiceImpl;


import com.tunahan.starter.DTO.*;
import com.tunahan.starter.exception.AccountNotFoundException;
import com.tunahan.starter.exception.WrongPasswordException;
import com.tunahan.starter.model.Account;
import com.tunahan.starter.model.User;
import com.tunahan.starter.repository.UserRepository;
import com.tunahan.starter.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserResponseDto register(UserRegisterDto registerDto){
        User user = new User();
        BeanUtils.copyProperties(registerDto,user);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Account account = new Account();
        account.setAccountNumber(UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        account.setBalance(BigDecimal.ZERO);
        account.setUser(user);

        user.setAccounts(new ArrayList<>(List.of(account)));
        User savedUser = userRepository.save(user);
        UserResponseDto responseDto = new UserResponseDto();
        BeanUtils.copyProperties(savedUser,responseDto);
        return responseDto;

    }

    public UserResponseDto userInformation(Long userId){
        User user = userRepository.findById(userId.intValue()).
                orElseThrow(() -> new AccountNotFoundException("Kullanıcı bulunamadı"));
        UserResponseDto userResponseDto = new UserResponseDto();
        BeanUtils.copyProperties(user,userResponseDto);
        return userResponseDto;
    }

    public void password(Integer userId,UserPasswordDto userPasswordDto ){
        User user = userRepository.findById(userId).
                orElseThrow(() -> new AccountNotFoundException("Kullanıcı bulunamadı"));
        if (passwordEncoder.matches(userPasswordDto.getOldPassword(),user.getPassword())){
            user.setPassword(passwordEncoder.encode(userPasswordDto.getNewPassword()));
            userRepository.save(user);
        }else{
            throw new WrongPasswordException("Şifreniz yanlıştır lütfen tekrar deneyiniz");
        }
    }
}
