package com.github.zhenya.accountingbot.service.handler;

import com.github.zhenya.accountingbot.cache.Cache;
import com.github.zhenya.accountingbot.entity.Account;
import com.github.zhenya.accountingbot.repository.AccountRepository;
import com.github.zhenya.accountingbot.constant.Status;
import com.github.zhenya.accountingbot.view.ViewComponent;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountEnterSumMessageHandler implements MessageRequestHandler {

    private final Cache cache;
    private final ViewComponent viewComponent;

    private final AccountRepository accountRepository;

    @Override
    public SendMessage handleMessageRequest(String value, Long chatId) {
        if (!NumberUtils.isCreatable(value)) {
            return new SendMessage(chatId, "Значення має бути цифровим");
        }
        Account account = cache.getAccount(chatId);
        if (account == null) {
            return new SendMessage(chatId, "Сталась помилка. Почніть створення рахунку знову");
        }
        account.setSum(Long.parseLong(value));
        accountRepository.save(account);
        cache.deleteStatus(chatId);
        cache.deleteAccount(chatId);
        List<Account> accounts = accountRepository.findAllByChatId(chatId);
        return viewComponent.getSuccessCreatedAccountMessage(accounts, chatId);
    }

    @Override
    public Status getStatus() {
        return Status.ENTER_ACCOUNT_SUM;
    }
}
