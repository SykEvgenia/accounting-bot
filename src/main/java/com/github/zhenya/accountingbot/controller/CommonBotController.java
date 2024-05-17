package com.github.zhenya.accountingbot.controller;

import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.github.zhenya.accountingbot.service.CommonService;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@BotController
@RequiredArgsConstructor
public class CommonBotController implements TelegramMvcController {

    private final CommonService commonService;

    @Value("${bot.token}")
    private String token;

    @MessageRequest(value = "{value}")
    public SendMessage handleMessageRequest(@BotPathVariable("value") String value, Chat chat) {
        return commonService.handleMessageRequest(value, chat.id());
    }

    @Override
    public String getToken() {
        return token;
    }
}
