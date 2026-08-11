package com.tunahan.starter.repository;

import com.tunahan.starter.model.Account;
import com.tunahan.starter.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByFromAccountOrToAccount(Account fromAccount, Account toAccount);
    }
