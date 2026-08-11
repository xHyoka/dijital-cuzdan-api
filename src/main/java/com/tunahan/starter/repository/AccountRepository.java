package com.tunahan.starter.repository;

import com.tunahan.starter.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {}
