package com.github.zhenya.accountingbot.service.handler;

import com.github.zhenya.accountingbot.constant.Status;
import com.pengrad.telegrambot.request.SendMessage;

public interface MessageRequestHandler {

    Status getStatus();

    SendMessage handleMessageRequest(String value, Long chatId);
}
