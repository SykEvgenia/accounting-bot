package com.github.zhenya.accountingbot.service.handler;

import com.github.zhenya.accountingbot.cache.Cache;
import com.github.zhenya.accountingbot.entity.Account;
import com.github.zhenya.accountingbot.entity.History;
import com.github.zhenya.accountingbot.repository.AccountRepository;
import com.github.zhenya.accountingbot.repository.HistoryRepository;
import com.github.zhenya.accountingbot.constant.Status;
import com.github.zhenya.accountingbot.view.ViewComponent;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoryEnterCommentMessageHandler implements MessageRequestHandler {

    private final Cache cache;
    private final ViewComponent viewComponent;
    private final HistoryRepository historyRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public SendMessage handleMessageRequest(String value, Long chatId) {
        History history = cache.getHistory(chatId);
        if (history == null) {
            return viewComponent.getErrorMessage(chatId);
        }
        history.setComment(value);
        historyRepository.save(history);
        Account account = history.getAccount();
        account.setSum(account.getSum() + history.getSum());
        accountRepository.save(account);
        cache.deleteStatus(chatId);
        cache.deleteHistory(chatId);
        return viewComponent.getAccountMessage(account, chatId);
    }

    @Override
    public Status getStatus() {
        return Status.ENTER_HISTORY_COMMENT;
    }
}
