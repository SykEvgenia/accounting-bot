package com.github.zhenya.accountingbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AccountingBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountingBotApplication.class, args);
    }
}
