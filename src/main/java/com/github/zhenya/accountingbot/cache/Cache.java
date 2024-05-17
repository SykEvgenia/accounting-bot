package com.github.zhenya.accountingbot.cache;

import com.github.zhenya.accountingbot.entity.Account;
import com.github.zhenya.accountingbot.entity.History;
import com.github.zhenya.accountingbot.constant.Status;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class Cache {

    @Cacheable(value = "status", key = "#chatId")
    public Status getStatus(Long chatId) {
        return null;
    }

    @CachePut(value = "status", key = "#chatId")
    public Status putStatus(Long chatId, Status status) {
        return status;
    }

    @CacheEvict(value = "status", key = "#chatId")
    public void deleteStatus(Long chatId) {
    }

    @Cacheable(value = "account", key = "#chatId")
    public Account getAccount(Long chatId) {
        return null;
    }

    @CachePut(value = "account", key = "#chatId")
    public Account putAccount(Long chatId, Account account) {
        return account;
    }

    @CacheEvict(value = "account", key = "#chatId")
    public void deleteAccount(Long chatId) {
    }

    @Cacheable(value = "history", key = "#chatId")
    public History getHistory(Long chatId) {
        return null;
    }

    @CachePut(value = "history", key = "#chatId")
    public History putHistory(Long chatId, History history) {
        return history;
    }

    @CacheEvict(value = "history", key = "#chatId")
    public void deleteHistory(Long chatId) {
    }
}