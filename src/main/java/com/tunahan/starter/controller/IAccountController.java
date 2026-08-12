package com.tunahan.starter.controller;

import com.tunahan.starter.DTO.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

public interface IAccountController {

    public AccountResponseDto deposit(@PathVariable Integer toAccountId,
                                      @RequestBody DepositRequestDto dto);

    public AccountResponseDto withdraw(@PathVariable Integer fromAccountId,
                                       @RequestBody WithDrawRequestDto dto);

    public AccountResponseDto transaction(@PathVariable Integer toAccountId,
                                          @RequestBody TransferRequestDto transferRequestDto);

    public List<TransactionResponseDto> transactionHistory(Integer accountId);

    public ResponseEntity<BigDecimal> getBalance (@PathVariable Long accountId);

}
