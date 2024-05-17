package com.github.zhenya.accountingbot.service.handler;

import com.github.zhenya.accountingbot.cache.Cache;
import com.github.zhenya.accountingbot.entity.History;
import com.github.zhenya.accountingbot.constant.Status;
import com.github.zhenya.accountingbot.view.ViewComponent;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoryEnterSumMessageHandler implements MessageRequestHandler {

    private final Cache cache;
    private final ViewComponent viewComponent;

    @Override
    public SendMessage handleMessageRequest(String value, Long chatId) {
        if (!NumberUtils.isCreatable(value)) {
            return viewComponent.getValueIntMessage(chatId);
        }
        History history = cache.getHistory(chatId);
        if (history == null) {
            return viewComponent.getErrorMessage(chatId);
        }
        history.setSum(Long.parseLong(value));
        cache.putStatus(chatId, Status.ENTER_HISTORY_COMMENT);
        return viewComponent.getHistoryCommentMessage(chatId);
    }

    @Override
    public Status getStatus() {
        return Status.ENTER_HISTORY_SUM;
    }
}
