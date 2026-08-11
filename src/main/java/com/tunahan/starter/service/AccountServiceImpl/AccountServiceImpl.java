package com.tunahan.starter.service.AccountServiceImpl;

import com.tunahan.starter.DTO.*;
import com.tunahan.starter.exception.AccountNotFoundException;
import com.tunahan.starter.exception.InsufficientBalanceException;
import com.tunahan.starter.model.Account;
import com.tunahan.starter.model.Transaction;
import com.tunahan.starter.model.TransactionStatus;
import com.tunahan.starter.model.TransactionType;
import com.tunahan.starter.repository.AccountRepository;
import com.tunahan.starter.repository.TransactionRepository;
import com.tunahan.starter.service.IAccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    public AccountResponseDto deposit(Integer toAccountId, DepositRequestDto dto) {
        Account account = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Hesap bulunamadı"));
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

    public AccountResponseDto withdraw(Integer fromAccountId, WithDrawRequestDto withDrawRequestDto){
        Account account = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Hesap bulunamadı"));
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
    public AccountResponseDto transaction(Integer fromAccountId,TransferRequestDto dto){
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Hesap bulunamadı"));
        if (fromAccount.getBalance().compareTo(dto.getAmount()) < 0 ){
            throw new InsufficientBalanceException()"Yetersiz bakiye");
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


    public List<TransactionResponseDto> transactionHistory(Integer accountId){

        Account transactionAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Hesap bulunamadı"));

        List<Transaction> transactionList = transactionRepository.
                findByFromAccountOrToAccount(transactionAccount,transactionAccount);
        List<TransactionResponseDto> transactionResponseDtos = new ArrayList<>();
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



}
