package com.github.zhenya.accountingbot.service.handler;

import com.github.zhenya.accountingbot.cache.Cache;
import com.github.zhenya.accountingbot.entity.Account;
import com.github.zhenya.accountingbot.constant.Status;
import com.github.zhenya.accountingbot.view.ViewComponent;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.github.zhenya.accountingbot.constant.Status.ENTER_ACCOUNT_SUM;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountEnterNameMessageHandler implements MessageRequestHandler {

    private final Cache cache;
    private final ViewComponent viewComponent;

    @Override
    public SendMessage handleMessageRequest(String value, Long chatId) {
        Account account = cache.getAccount(chatId);
        if (account == null) {
            return viewComponent.getErrorMessage(chatId);
        }
        account.setName(value);
        cache.putAccount(chatId, account);
        cache.putStatus(chatId, ENTER_ACCOUNT_SUM);
        return viewComponent.getEnterSumMessage(chatId);
    }

    @Override
    public Status getStatus() {
        return Status.ENTER_ACCOUNT_NAME;
    }
}
