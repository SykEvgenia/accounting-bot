package com.github.zhenya.accountingbot.service;

import com.github.zhenya.accountingbot.cache.Cache;
import com.github.zhenya.accountingbot.entity.Account;
import com.github.zhenya.accountingbot.repository.AccountRepository;
import com.github.zhenya.accountingbot.view.ViewComponent;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.github.zhenya.accountingbot.constant.Status.ENTER_ACCOUNT_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final Cache cache;
    private final ViewComponent viewComponent;

    public SendMessage getAccounts(Long chatId) {
        List<Account> accounts = accountRepository.findAllByChatId(chatId);
        return viewComponent.getAccountsMessage(chatId, accounts);
    }

    public SendMessage createAccount(Long chatId) {
        cache.putAccount(chatId, new Account(chatId));
        return viewComponent.getEnterCurrencyAccountMessage(chatId);
    }

    public SendMessage fillAccountCurrency(String currency, Long chatId) {
        cache.putStatus(chatId, ENTER_ACCOUNT_NAME);
        Account account = cache.getAccount(chatId);
        if (account == null) {
            log.error("Account is absent in cache");
            return new SendMessage(chatId, "Сталась помилка. Почніть створення рахунку знову");
        }
        account.setCurrency(currency);
        cache.putAccount(chatId, account);
        return viewComponent.getEnterNameAccountMessage(chatId);
    }

    public SendMessage getAccountMessage(Long accountId, Long chatId) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        return viewComponent.getAccountMessage(account, chatId);
    }

    public SendMessage deleteAccount(Long accountId, Long chatId) {
        accountRepository.deleteById(accountId);
        List<Account> accounts = accountRepository.findAllByChatId(chatId);
        return viewComponent.getAccountsMessage(chatId, accounts);
    }
}
