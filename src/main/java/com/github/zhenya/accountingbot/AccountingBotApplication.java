package com.github.zhenya.accountingbot;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

@EnableCaching
@SpringBootApplication
public class AccountingBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountingBotApplication.class, args);
    }

}
