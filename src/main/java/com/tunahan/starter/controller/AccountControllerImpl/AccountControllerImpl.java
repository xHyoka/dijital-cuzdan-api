package com.tunahan.starter.controller.AccountControllerImpl;

import com.tunahan.starter.DTO.*;
import com.tunahan.starter.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/account/api")
public class AccountControllerImpl {

    @Autowired
    private IAccountService accountService;

    @PostMapping(path = "/deposit/{toAccountId}")
    public AccountResponseDto deposit(@PathVariable Integer toAccountId,
                                      @RequestBody DepositRequestDto dto) {
        return accountService.deposit(toAccountId, dto);
    }

    @PostMapping(path = "/withdraw/{fromAccountId}")
    public AccountResponseDto withdraw(@PathVariable Integer fromAccountId,
                                       @RequestBody WithDrawRequestDto dto){
        return accountService.withdraw(fromAccountId,dto);
    }

    @PostMapping(path = "/transaction/{fromAccountId}")
    public AccountResponseDto transaction(@PathVariable Integer fromAccountId,
                                          @RequestBody TransferRequestDto transferRequestDto){
        return accountService.transaction(fromAccountId,transferRequestDto);
    }

    @GetMapping(path = "/history/{accountId}")
    public List<TransactionResponseDto> transactionHistory(@PathVariable Integer accountId){
        return accountService.transactionHistory(accountId);
    }
    @Cacheable(value = "balance", key = "#accountId")
    @GetMapping(path = "/balance/{accountId}")
    public ResponseEntity<BigDecimal> getBalance (@PathVariable Long accountId){
        return ResponseEntity.ok(accountService.getBalance(accountId));
    }

    @PostMapping("/create/{userId}")
    public AccountResponseDto createAccount(@PathVariable Integer userId) {
        return accountService.createAccount(userId);
    }

}