package com.github.zhenya.accountingbot.controller;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.request.CallbackQueryRequest;
import com.github.zhenya.accountingbot.service.HistoryService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@BotController
@RequiredArgsConstructor
public class HistoryBotController implements TelegramMvcController {

    private final HistoryService historyService;

    @Value("${bot.token}")
    private String token;

    @CallbackQueryRequest(value = "GET /account/{accountId}/history")
    public SendMessage getHistory(@BotPathVariable("accountId") Long accountId, Chat chat, TelegramBot bot, CallbackQuery callbackQuery) {
        deletePreviousMessage(chat.id(), bot, callbackQuery);
        return historyService.getHistories(accountId, chat.id());
    }

    @CallbackQueryRequest(value = "POST /account/{accountId}/history")
    public SendMessage createHistory(@BotPathVariable("accountId") Long accountId, Chat chat, TelegramBot bot, CallbackQuery callbackQuery) {
        deletePreviousMessage(chat.id(), bot, callbackQuery);
        return historyService.createHistory(accountId, chat.id());
    }

    @Override
    public String getToken() {
        return token;
    }

    private void deletePreviousMessage(Long chatId, TelegramBot bot, CallbackQuery callbackQuery) {
        bot.execute(new DeleteMessage(chatId, callbackQuery.message().messageId()));
    }
}
