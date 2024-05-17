package com.github.zhenya.accountingbot.controller;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.github.zhenya.accountingbot.service.AccountService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@BotController
@RequiredArgsConstructor
public class AccountBotController implements TelegramMvcController {

    private final AccountService accountService;

    @Value("${bot.token}")
    private String token;

    @MessageRequest(value = "/start")
    public SendMessage start(Chat chat) {
        return accountService.getAccounts(chat.id());
    }

    @CallbackQueryRequest(value = "GET /account")
    public SendMessage getAccounts(Chat chat, TelegramBot bot, CallbackQuery callbackQuery) {
        deletePreviousMessage(chat, bot, callbackQuery);
        return accountService.getAccounts(chat.id());
    }

    @CallbackQueryRequest(value = "POST /account")
    public SendMessage createAccount(Chat chat, TelegramBot bot, CallbackQuery callbackQuery) {
        deletePreviousMessage(chat, bot, callbackQuery);
        return accountService.createAccount(chat.id());
    }

    @CallbackQueryRequest(value = "PUT /account/currency/{currency}")
    public SendMessage fillAccountCurrency(@BotPathVariable("currency") String currency, Chat chat, TelegramBot bot, CallbackQuery callbackQuery) {
        deletePreviousMessage(chat, bot, callbackQuery);
        return accountService.fillAccountCurrency(currency, chat.id());
    }

    @CallbackQueryRequest(value = "GET /account/{accountId}")
    public SendMessage getAccount(@BotPathVariable("accountId") Long accountId, Chat chat, TelegramBot bot, CallbackQuery callbackQuery) {
        deletePreviousMessage(chat, bot, callbackQuery);
        return accountService.getAccount(accountId, chat.id());
    }

    @CallbackQueryRequest(value = "DELETE /account/{accountId}")
    public SendMessage deleteAccount(@BotPathVariable("accountId") Long accountId, Chat chat, TelegramBot bot, CallbackQuery callbackQuery) {
        deletePreviousMessage(chat, bot, callbackQuery);
        return accountService.deleteAccount(accountId, chat.id());
    }

    @Override
    public String getToken() {
        return token;
    }

    private void deletePreviousMessage(Chat chat, TelegramBot bot, CallbackQuery callbackQuery) {
        bot.execute(new DeleteMessage(chat.id(), callbackQuery.message().messageId()));
    }
}