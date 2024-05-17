package com.github.zhenya.accountingbot.service;

import com.github.zhenya.accountingbot.cache.Cache;
import com.github.zhenya.accountingbot.entity.Account;
import com.github.zhenya.accountingbot.entity.History;
import com.github.zhenya.accountingbot.repository.AccountRepository;
import com.github.zhenya.accountingbot.repository.HistoryRepository;
import com.github.zhenya.accountingbot.constant.Status;
import com.github.zhenya.accountingbot.view.ViewComponent;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final Cache cache;
    private final HistoryRepository historyRepository;
    private final AccountRepository accountRepository;
    private final ViewComponent viewComponent;

    public SendMessage getHistories(Long accountId, Long chatId) {
        List<History> histories = historyRepository.findAllByAccountId(accountId);
        return viewComponent.getHistories(histories, chatId, accountId);
    }

    public SendMessage createHistory(Long accountId, Long chatId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isPresent()) {
            cache.putHistory(chatId, new History(account.get()));
            cache.putStatus(chatId, Status.ENTER_HISTORY_SUM);
            return viewComponent.getHistorySumMessage(chatId);
        }
        return viewComponent.getErrorMessage(chatId);
    }
}
