package com.github.zhenya.accountingbot.service;

import com.github.zhenya.accountingbot.cache.Cache;
import com.github.zhenya.accountingbot.service.handler.MessageRequestHandler;
import com.github.zhenya.accountingbot.constant.Status;
import com.github.zhenya.accountingbot.view.ViewComponent;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final Cache cache;
    private final ViewComponent viewComponent;

    private Map<Status, MessageRequestHandler> messageRequestHandlers;

    public SendMessage handleMessageRequest(String value, Long chatId) {
        Status status = cache.getStatus(chatId);
        return status == null ?
                viewComponent.getErrorMessage(chatId)
                : messageRequestHandlers.get(status).handleMessageRequest(value, chatId);
    }

    @Autowired
    public void setMessageRequestHandlers(List<MessageRequestHandler> messageRequestHandlers) {
        this.messageRequestHandlers = messageRequestHandlers.stream()
                .collect(Collectors.toMap(MessageRequestHandler::getStatus, Function.identity()));
    }
}
