package com.github.zhenya.accountingbot.service.handler;

import com.github.zhenya.accountingbot.cache.Cache;
import com.github.zhenya.accountingbot.constant.Status;
import com.github.zhenya.accountingbot.entity.History;
import com.github.zhenya.accountingbot.view.ViewComponent;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.github.zhenya.accountingbot.constant.Status.ENTER_HISTORY_COMMENT;
import static com.github.zhenya.accountingbot.constant.Status.ENTER_HISTORY_SUM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoryEnterSumMessageHandlerTest {

    @Mock
    private Cache cache;
    @Mock
    private ViewComponent viewComponent;

    @InjectMocks
    private HistoryEnterSumMessageHandler handler;

    @Captor
    ArgumentCaptor<Status> statusCaptor;

    @Test
    void handleMessageRequest_expectSuccess() {
        when(cache.getHistory(123456L)).thenReturn(new History());
        when(viewComponent.getHistoryCommentMessage(123456L)).thenReturn(new SendMessage(123456L, ""));

        SendMessage sendMessage = handler.handleMessageRequest("12", 123456L);

        assertNotNull(sendMessage);
        verify(cache).getHistory(123456L);
        verify(cache).putStatus(eq(123456L), statusCaptor.capture());
        assertEquals(ENTER_HISTORY_COMMENT, statusCaptor.getValue());
    }

    @Test
    void handleMessageRequest_expectErrorMessageWhenSumValueIsInvalid() {
        handler.handleMessageRequest("history", 123456L);

        verifyNoInteractions(cache);
        verifyNoInteractions(viewComponent);
    }

    @Test
    void handleMessageRequest_expectErrorMessageWhenAccountInCacheIsAbsent() {
        SendMessage sendMessage = handler.handleMessageRequest("12", 123456L);

        assertNotNull(sendMessage);
        verify(cache).getHistory(123456L);
        verifyNoInteractions(viewComponent);
    }

    @Test
    void getStatus_expectSuccess() {
        assertEquals(ENTER_HISTORY_SUM, handler.getStatus());
    }
}
