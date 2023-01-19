package com.github.zhenya.accountingbot.cache;

import com.github.zhenya.accountingbot.entity.Account;
import com.github.zhenya.accountingbot.entity.History;
import com.github.zhenya.accountingbot.constant.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Cache {

    @CachePut(value = "userAccounts", key = "#id")
    public Account putAccount(Long id, Account account) {
        return account;
    }

    @Cacheable(value = "userAccounts", key = "#id")
    public Account getAccount(Long id) {
        return null;
    }

    @CacheEvict(value = "userAccounts", key = "#id")
    public void deleteAccount(Long id) {
    }

    @Cacheable(value = "userStatus", key = "#id")
    public Status getStatus(Long id) {
        return null;
    }

    @CachePut(value = "userStatus", key = "#id")
    public Status putStatus(Long id, Status status) {
        return status;
    }

    @CacheEvict(value = "userStatus", key = "#id")
    public void deleteStatus(Long id) {
    }

    @CachePut(value = "userHistory", key = "#id")
    public History putHistory(Long id, History history) {
        return history;
    }

    @Cacheable(value = "userHistory", key = "#id")
    public History getHistory(Long id) {
        return null;
    }

    @CacheEvict(value = "userHistory", key = "#id")
    public void deleteHistory(Long id) {
    }
}

