package com.tunahan.starter;


import com.tunahan.starter.DTO.*;
import com.tunahan.starter.exception.AccountNotFoundException;
import com.tunahan.starter.exception.InsufficientBalanceException;
import com.tunahan.starter.model.Account;
import com.tunahan.starter.model.Transaction;
import com.tunahan.starter.model.TransactionStatus;
import com.tunahan.starter.model.TransactionType;
import com.tunahan.starter.repository.AccountRepository;
import com.tunahan.starter.repository.TransactionRepository;
import com.tunahan.starter.service.AccountServiceImpl.AccountServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    @DisplayName("deposit: Geçerli hesaba para yatırılmalı")
    void deposit_ShouldIncreaseBalance(){
        Account account = new Account();
        account.setId(1);
        account.setBalance(BigDecimal.valueOf(100));

        DepositRequestDto dto = new DepositRequestDto();
        dto.setAmount(BigDecimal.valueOf(50));

        when(accountRepository.findById(1)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountResponseDto result = accountService.deposit(1,dto);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(150) , account.getBalance());
        verify(transactionRepository,times(1)).save(any(Transaction.class));

    }

    @Test
    @DisplayName("deposit: Hesap bulunamazsa exception fırlatmalı")
    void deposit_WhenAccountNotFound_ShouldThrowException(){
        when(accountRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> accountService.deposit(99,new DepositRequestDto()));
    }


    @Test
    @DisplayName("withdraw: Geçerli hesaptan para azalmalı")
    void withdraw_ShouldDecreaseBalance(){
        Account account = new Account();
        account.setId(1);
        account.setBalance(BigDecimal.valueOf(100));

        WithDrawRequestDto withDrawRequestDto = new WithDrawRequestDto();
        withDrawRequestDto.setAmount(BigDecimal.valueOf(50));

        when(accountRepository.findById(1)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountResponseDto result = accountService.withdraw(1,withDrawRequestDto);

        assertNotNull(result);
        assertEquals(account.getBalance(),BigDecimal.valueOf(50));
        verify(transactionRepository,times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("withdraw: Hesap bulunamazsa exception fırlatılmalı")
    void withdraw_WhenAccountNotFound_ShouldThrowException() {
        when(accountRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> accountService.withdraw(99, new WithDrawRequestDto()));
    }

    @Test
    @DisplayName("withdraw: Yeterli bakiye yoksa exception fırlatmalı")
    void withdraw_WhenInsufficientBalance_ShouldThrowException(){
        Account account = new Account();
        account.setId(1);
        account.setBalance(BigDecimal.valueOf(30));

        WithDrawRequestDto dto = new WithDrawRequestDto();
        dto.setAmount(BigDecimal.valueOf(100));

        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        assertThrows(InsufficientBalanceException.class,
                () -> accountService.withdraw(1,dto));

    }

    @Test
    @DisplayName("transfer: Gönderen Hesaptan Bakiye Eksilmeli Alıcı Hesapta Artmalı")
    void transfer_SendersBalanceShouldDecreaseReceiversShouldIncrease(){
        Account fromAccount = new Account();
        fromAccount.setId(1);
        fromAccount.setBalance(BigDecimal.valueOf(100));

        Account toAccount = new Account();
        toAccount.setId(2);
        toAccount.setBalance(BigDecimal.valueOf(50));

        TransferRequestDto dto = new TransferRequestDto();
        dto.setToAccountId(2);
        dto.setAmount(BigDecimal.valueOf(30));


        when(accountRepository.findById(1)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2)).thenReturn(Optional.of(toAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(fromAccount);

        AccountResponseDto result = accountService.transaction(1,dto);


        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(70),fromAccount.getBalance());
        assertEquals(BigDecimal.valueOf(80),toAccount.getBalance());
        verify(accountRepository,times(2)).save(any(Account.class));
        verify(transactionRepository,times(1)).save(any(Transaction.class));
    }


    @Test
    @DisplayName("transaction: Gönderen hesap bulunamazsa exception fırlatılmalı")
    void transaction_WhenFromAccountNotFound_ShouldThrowException() {
        when(accountRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> accountService.transaction(99, new TransferRequestDto()));
    }

    @Test
    @DisplayName("transaction: Yetersiz bakiyede exception fırlatılmalı")
    void transaction_WhenInsufficientBalance_ShouldThrowException() {
        Account fromAccount = new Account();
        fromAccount.setId(1);
        fromAccount.setBalance(BigDecimal.valueOf(20));

        TransferRequestDto dto = new TransferRequestDto();
        dto.setToAccountId(2);
        dto.setAmount(BigDecimal.valueOf(100));

        when(accountRepository.findById(1)).thenReturn(Optional.of(fromAccount));

        assertThrows(InsufficientBalanceException.class,
                () -> accountService.transaction(1, dto));
    }

    @Test
    @DisplayName("transaction: Alıcı hesap bulunamazsa exception fırlatılmalı")
    void transaction_WhenToAccountNotFound_ShouldThrowException() {
        Account fromAccount = new Account();
        fromAccount.setId(1);
        fromAccount.setBalance(BigDecimal.valueOf(100));


        when(accountRepository.findById(1)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(99)).thenReturn(Optional.empty());

        TransferRequestDto dto = new TransferRequestDto();
        dto.setToAccountId(99);
        dto.setAmount(BigDecimal.valueOf(50));

        assertThrows(AccountNotFoundException.class,
                () -> accountService.transaction(1, dto));
    }


    @Test
    @DisplayName("transactionHistory: Geçerli hesabın işlem geçmişi dönmeli")
    void transactionHistory_ShouldReturnTransactions(){
        Account account = new Account();
        account.setId(1);

        Transaction t1 = new Transaction();
        t1.setId(1);
        t1.setAmount(BigDecimal.valueOf(100));
        t1.setType(TransactionType.DEPOSIT);
        t1.setStatus(TransactionStatus.SUCCESS);
        t1.setTimestamp(LocalDateTime.now());
        t1.setFromAccount(null);
        t1.setToAccount(account);

        Transaction t2 = new Transaction();
        t2.setId(2);
        t2.setAmount(BigDecimal.valueOf(50));
        t2.setType(TransactionType.WITHDRAW);
        t2.setStatus(TransactionStatus.SUCCESS);
        t2.setTimestamp(LocalDateTime.now());
        t2.setFromAccount(account);
        t2.setToAccount(null);

        when(accountRepository.findById(1)).thenReturn(Optional.of(account));
        when(transactionRepository.findByFromAccountOrToAccount(account,account))
                .thenReturn(List.of(t1,t2));
        List<TransactionResponseDto> result = accountService.transactionHistory(1);

        assertNotNull(result);
        assertEquals(2,result.size());
        verify(transactionRepository,times(1))
                .findByFromAccountOrToAccount(account,account);

    }

    @Test
    @DisplayName("transactionHistory: Hesap bulunamazsa exception fırlatılmalı")
    void transactionHistory_WhenAccountNotFound_ShouldThrowException() {
        when(accountRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> accountService.transactionHistory(99));
    }

    @Test
    @DisplayName("getBalance: Hesap bakiyesini göstermeli")
    void getBalance_ShouldShowAccountBalance(){
        Account account = new Account();
        account.setId(1);
        account.setBalance(BigDecimal.valueOf(50));
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        assertEquals(BigDecimal.valueOf(50),accountService.getBalance(1L));

    }

    @Test
    @DisplayName("getBalance: Hesap bulunamayınca hata vermeli")
    void getBalance_ShouldThrowExceptionWhenAccountNotFound() {
        when(accountRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> accountService.getBalance(99L));
    }

}
