package com.github.zhenya.accountingbot.repository;

import com.github.zhenya.accountingbot.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findAllByAccountId(Long id);

    void deleteAllByAccountId(Long accountId);
}
