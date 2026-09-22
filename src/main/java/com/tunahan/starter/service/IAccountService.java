package com.tunahan.starter.service;

import com.tunahan.starter.DTO.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

public interface IAccountService {

    public AccountResponseDto transaction(@PathVariable Integer toAccountId,
                                      @RequestBody TransferRequestDto dto);

    public AccountResponseDto deposit(Integer toAccountId, DepositRequestDto dto);

    public AccountResponseDto withdraw(Integer fromAccountId, WithDrawRequestDto dto);

    public List<TransactionResponseDto> transactionHistory(Integer accountId);

    public BigDecimal getBalance(Long accountId);

    public AccountResponseDto createAccount(Integer userId);
}
