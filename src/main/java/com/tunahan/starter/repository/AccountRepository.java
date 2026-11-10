package com.tunahan.starter.repository;

import com.tunahan.starter.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByIdAndUserUsername(Integer id, String username);
}
