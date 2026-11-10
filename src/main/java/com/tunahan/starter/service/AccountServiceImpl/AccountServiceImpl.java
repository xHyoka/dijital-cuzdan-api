package com.tunahan.starter.service.AccountServiceImpl;

import com.tunahan.starter.DTO.*;
import com.tunahan.starter.exception.AccountNotFoundException;
import com.tunahan.starter.exception.InsufficientBalanceException;
import com.tunahan.starter.exception.InvalidTransitionException;
import com.tunahan.starter.model.*;
import com.tunahan.starter.repository.AccountRepository;
import com.tunahan.starter.repository.TransactionRepository;
import com.tunahan.starter.repository.UserRepository;
import com.tunahan.starter.service.IAccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public AccountResponseDto deposit(Integer toAccountId, DepositRequestDto dto,String username) {
        Account account = getOwnedAccount(toAccountId,username);
        account.setBalance(account.getBalance().add(dto.getAmount()));
        Account savedAccount = accountRepository.save(account);
        Transaction transaction = new Transaction();
        transaction.setFromAccount(null);
        transaction.setToAccount(account);
        transaction.setAmount(dto.getAmount());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
        AccountResponseDto accountResponseDto = new AccountResponseDto();
        BeanUtils.copyProperties(savedAccount,accountResponseDto);
        return accountResponseDto;
    }

    @Transactional
    public AccountResponseDto withdraw(Integer fromAccountId, WithDrawRequestDto withDrawRequestDto,String username){
        Account account = getOwnedAccount(fromAccountId,username);
        if (account.getBalance().compareTo(withDrawRequestDto.getAmount()) < 0) {
            throw new InsufficientBalanceException("Yetersiz bakiye");
        }
        account.setBalance(account.getBalance().subtract(withDrawRequestDto.getAmount()));
        Account savedAccount = accountRepository.save(account);
        Transaction transaction = new Transaction();
        transaction.setFromAccount(account);
        transaction.setToAccount(null);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setAmount(withDrawRequestDto.getAmount());
        transaction.setType(TransactionType.WITHDRAW);
        transactionRepository.save(transaction);
        AccountResponseDto accountResponseDto = new AccountResponseDto();
        BeanUtils.copyProperties(savedAccount,accountResponseDto);
        return accountResponseDto;
    }

    @Transactional
    public AccountResponseDto transaction(Integer fromAccountId,TransferRequestDto dto,String username){

        if (fromAccountId.equals(dto.getToAccountId())){
            throw new InvalidTransitionException("Kendi hesabına transfer işlemi yapamazsın.");
        }
        Account fromAccount = getOwnedAccount(fromAccountId,username);
        if (fromAccount.getBalance().compareTo(dto.getAmount()) < 0 ){
            throw new InsufficientBalanceException("Yetersiz bakiye");
        }
    fromAccount.setBalance(fromAccount.getBalance().subtract(dto.getAmount()));
        Account toAccount = accountRepository.findById(dto.getToAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Hesap bulunamadı"));
        toAccount.setBalance(toAccount.getBalance().add(dto.getAmount()));
        AccountResponseDto accountResponseDto = new AccountResponseDto();
        Account savedAccount = accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(dto.getAmount());
        transaction.setType(TransactionType.TRANSFER);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
        BeanUtils.copyProperties(savedAccount,accountResponseDto);
        return accountResponseDto;
    }


    public List<TransactionResponseDto> transactionHistory(Integer accountId,String username){

        Account transactionAccount = getOwnedAccount(accountId,username);

        List<Transaction> transactionList = transactionRepository.
                findByFromAccountOrToAccountOrderByTimestampDesc(transactionAccount, transactionAccount);        List<TransactionResponseDto> transactionResponseDtos = new ArrayList<>();
        for (Transaction transaction : transactionList){
            TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
            BeanUtils.copyProperties(transaction,transactionResponseDto);
            if (transaction.getFromAccount() != null) {
                transactionResponseDto.setFromAccountId(transaction.getFromAccount().getId());
            }
            if (transaction.getToAccount() != null) {
                transactionResponseDto.setToAccountId(transaction.getToAccount().getId());
            }

            transactionResponseDtos.add(transactionResponseDto);

        }
    return transactionResponseDtos;
    }

    public BigDecimal getBalance (Integer accountId,String username){
        Account account = getOwnedAccount(accountId,username);
        return account.getBalance();
    }

    public AccountResponseDto createAccount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AccountNotFoundException("Kullanıcı bulunamadı"));

        Account account = new Account();
        account.setAccountNumber(UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        account.setBalance(BigDecimal.ZERO);
        account.setUser(user);

        Account saved = accountRepository.save(account);
        AccountResponseDto dto = new AccountResponseDto();
        BeanUtils.copyProperties(saved, dto);
        return dto;
    }




    private Account getOwnedAccount(Integer accountId, String username) {
        return accountRepository.findByIdAndUserUsername(accountId, username)
                .orElseThrow(() -> new AccountNotFoundException("Hesap bulunamadı"));
    }

}
