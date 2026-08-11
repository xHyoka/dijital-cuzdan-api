package com.tunahan.starter.repository;


import com.tunahan.starter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {}
