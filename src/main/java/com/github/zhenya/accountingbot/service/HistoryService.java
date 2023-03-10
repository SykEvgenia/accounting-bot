package com.github.zhenya.accountingbot.service;

import com.github.zhenya.accountingbot.cache.Cache;
import com.github.zhenya.accountingbot.entity.History;
import com.github.zhenya.accountingbot.repository.HistoryRepository;
import com.github.zhenya.accountingbot.constant.Status;
import com.github.zhenya.accountingbot.view.ViewComponent;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final Cache cache;
    private final HistoryRepository historyRepository;
    private final ViewComponent viewComponent;

    public SendMessage getHistories(Long accountId, Long chatId) {
        List<History> histories = historyRepository.findAllByAccountId(accountId);
        return viewComponent.getHistories(histories, chatId, accountId);
    }

    public SendMessage createHistory(Long accountId, Long chatId) {
        cache.putHistory(chatId, new History(accountId));
        cache.putStatus(chatId, Status.ENTER_HISTORY_SUM);
        return viewComponent.getHistorySumMessage(chatId);
    }
}
