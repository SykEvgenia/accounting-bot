package com.github.zhenya.accountingbot.repository;

import com.github.zhenya.accountingbot.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account,Long> {

    List<Account> findAllByChatId(Long id);
}
