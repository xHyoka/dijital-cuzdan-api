package com.tunahan.starter.service.ServiceImpl;


import com.tunahan.starter.DTO.AccountResponseDto;
import com.tunahan.starter.DTO.DepositRequestDto;
import com.tunahan.starter.DTO.UserRegisterDto;
import com.tunahan.starter.DTO.UserResponseDto;
import com.tunahan.starter.model.Account;
import com.tunahan.starter.model.User;
import com.tunahan.starter.repository.UserRepository;
import com.tunahan.starter.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;



    public UserResponseDto register(UserRegisterDto registerDto){
        User user = new User();
        BeanUtils.copyProperties(registerDto,user);
        Account account = new Account();
        account.setAccountNumber(UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        account.setBalance(BigDecimal.ZERO);
        account.setUser(user);
        user.setAccount(account);
        User savedUser = userRepository.save(user);
        UserResponseDto responseDto = new UserResponseDto();
        BeanUtils.copyProperties(savedUser,responseDto);
        return responseDto;

    }




}
