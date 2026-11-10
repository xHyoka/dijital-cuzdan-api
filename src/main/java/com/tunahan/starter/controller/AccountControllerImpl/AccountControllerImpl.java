package com.tunahan.starter.controller.AccountControllerImpl;

import com.tunahan.starter.DTO.*;
import com.tunahan.starter.service.IAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
                                      @Valid @RequestBody DepositRequestDto dto,
                                      @AuthenticationPrincipal UserDetails currentUser) {
        return accountService.deposit(toAccountId, dto,currentUser.getUsername());
    }

    @PostMapping(path = "/withdraw/{fromAccountId}")
    public AccountResponseDto withdraw(@PathVariable Integer fromAccountId,
                                       @Valid @RequestBody WithDrawRequestDto dto,
                                       @AuthenticationPrincipal UserDetails currentUser){
        return accountService.withdraw(fromAccountId,dto,currentUser.getUsername());
    }

    @PostMapping(path = "/transaction/{fromAccountId}")
    public AccountResponseDto transaction(@PathVariable Integer fromAccountId,
                                          @Valid @RequestBody TransferRequestDto transferRequestDto,
                                          @AuthenticationPrincipal UserDetails currentUser){
        return accountService.transaction(fromAccountId,transferRequestDto,currentUser.getUsername());
    }

    @GetMapping(path = "/history/{accountId}")
    public List<TransactionResponseDto> transactionHistory(@PathVariable Integer accountId,
                                                           @AuthenticationPrincipal UserDetails currentUser){
        return accountService.transactionHistory(accountId,currentUser.getUsername());
    }
    @GetMapping(path = "/balance/{accountId}")
    public ResponseEntity<BigDecimal> getBalance (@PathVariable Integer accountId,
                                                  @AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.ok(accountService.getBalance(accountId,currentUser.getUsername()));
    }

    @PostMapping("/create")
    public AccountResponseDto createAccount(@AuthenticationPrincipal UserDetails currentUser) {
        return accountService.createAccount(currentUser.getUsername());
    }

}