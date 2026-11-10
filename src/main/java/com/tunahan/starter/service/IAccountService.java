package com.tunahan.starter.service;

import com.tunahan.starter.DTO.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

public interface IAccountService {

    public AccountResponseDto transaction( Integer fromAcountId,
                                       TransferRequestDto dto,
                                          String username);

    public AccountResponseDto deposit(Integer toAccountId, DepositRequestDto dto,
                                      String username);

    public AccountResponseDto withdraw(Integer fromAccountId, WithDrawRequestDto dto,
                                       String username);

    public List<TransactionResponseDto> transactionHistory(Integer accountId,
                                                           String username);

    public BigDecimal getBalance(Integer accountId,
                                 String username);

    public AccountResponseDto createAccount(Integer userId);
}
