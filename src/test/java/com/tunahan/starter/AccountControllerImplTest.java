package com.tunahan.starter;

import com.tunahan.starter.DTO.*;
import com.tunahan.starter.controller.AccountControllerImpl.AccountControllerImpl;
import com.tunahan.starter.controller.AuthControllerImpl.AuthControllerImpl;
import com.tunahan.starter.repository.UserRepository;
import com.tunahan.starter.security.CustomUserDetailService;
import com.tunahan.starter.security.JwtAuthFilter;
import com.tunahan.starter.security.SecurityConfig;
import com.tunahan.starter.service.IAccountService;
import com.tunahan.starter.service.TokenBlacklistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import java.util.List;
import java.math.BigDecimal;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountControllerImpl.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        SecurityConfig.class,
                        AuthControllerImpl.class,
                        JwtAuthFilter.class,
                        CustomUserDetailService.class,
                        TokenBlacklistService.class
                }
        ))
@AutoConfigureMockMvc(addFilters = false)
public class AccountControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IAccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private StringRedisTemplate stringRedisTemplate;

    @Test
    @DisplayName("deposit: 150 dönmeli")
    void deposit_ShouldReturn150() throws Exception{
        DepositRequestDto dto = new DepositRequestDto();
        dto.setAmount(BigDecimal.valueOf(50));

        AccountResponseDto response = new AccountResponseDto();
        response.setBalance(BigDecimal.valueOf(150));

        when(accountService.deposit(eq(1),any(DepositRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/account/api/deposit/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(150));


    }


    @Test
    @DisplayName("withdraw: 50 dönmeli")
    void withdraw_ShouldReturn50() throws Exception {
        WithDrawRequestDto dto = new WithDrawRequestDto();
        dto.setAmount(BigDecimal.valueOf(50));

        AccountResponseDto response = new AccountResponseDto();
        response.setBalance(BigDecimal.valueOf(50));

        when(accountService.withdraw(eq(1), any(WithDrawRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/account/api/withdraw/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(50));
    }


    @Test
    @DisplayName("transaction: 70 dönmeli")
    void transaction_ShouldReturn70() throws Exception {
        TransferRequestDto dto = new TransferRequestDto();
        dto.setToAccountId(2);
        dto.setAmount(BigDecimal.valueOf(30));

        AccountResponseDto response = new AccountResponseDto();
        response.setBalance(BigDecimal.valueOf(70));

        when(accountService.transaction(eq(1), any(TransferRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/account/api/transaction/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(70));
    }


    @Test
    @DisplayName("getBalance: 100 dönmeli")
    void getBalance_ShouldReturn100() throws Exception {
        when(accountService.getBalance(1L))
                .thenReturn(BigDecimal.valueOf(100));

        mockMvc.perform(get("/account/api/balance/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(100));
    }


    @Test
    @DisplayName("createAccount: 0 dönmeli")
    void createAccount_ShouldReturn0() throws Exception {
        AccountResponseDto response = new AccountResponseDto();
        response.setBalance(BigDecimal.ZERO);

        when(accountService.createAccount(1)).thenReturn(response);

        mockMvc.perform(post("/account/api/create/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(0));
    }

    @Test
    @DisplayName("transactionHistory: 100 dönmeli")
    void transactionHistory_ShouldReturn100() throws Exception {
        TransactionResponseDto t1 = new TransactionResponseDto();
        t1.setAmount(BigDecimal.valueOf(100));

        when(accountService.transactionHistory(1))
                .thenReturn(List.of(t1));

        mockMvc.perform(get("/account/api/history/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));



    }
}