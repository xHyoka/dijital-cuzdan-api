package com.tunahan.starter.controller;

import com.tunahan.starter.service.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/redis/test")
@RequiredArgsConstructor
public class RedisCacheController {

    private final RedisCacheService redisCacheService;

    @GetMapping
    public String cacheControl() throws InterruptedException{
        return redisCacheService.longRunningMethod();
    }
}
