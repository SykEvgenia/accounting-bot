package com.github.zhenya.accountingbot.service;

import com.github.zhenya.accountingbot.cache.Cache;
import com.github.zhenya.accountingbot.service.handler.MessageRequestHandler;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.github.zhenya.accountingbot.constant.Status.ENTER_ACCOUNT_SUM;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommonServiceTest {

    @Mock
    private Cache cache;
    @Mock
    private MessageRequestHandler messageRequestHandler;
    private final List<MessageRequestHandler> messageRequestHandlers = getMessageRequestHandlers();
    @InjectMocks
    private CommonService service;

    @Test
    void handleMessageRequest_expectSuccess() {
        when(messageRequestHandler.getStatus()).thenReturn(ENTER_ACCOUNT_SUM);
        when(messageRequestHandler.handleMessageRequest("123", 123456L)).thenReturn(new SendMessage(123456L, ""));
        service.setMessageRequestHandlers(messageRequestHandlers);
        when(cache.getStatus(123456L)).thenReturn(ENTER_ACCOUNT_SUM);

        SendMessage sendMessage = service.handleMessageRequest("123", 123456L);

        assertNotNull(sendMessage);
        verify(cache).getStatus(123456L);
        verify(messageRequestHandler).handleMessageRequest("123", 123456L);
    }

    @Test
    void handleMessageRequest() {
        SendMessage sendMessage = service.handleMessageRequest("123", 123456L);

        assertNotNull(sendMessage);
        verifyNoInteractions(messageRequestHandler);
    }

    @NotNull
    private List<MessageRequestHandler> getMessageRequestHandlers() {
        List<MessageRequestHandler> messageRequestHandlers = new ArrayList<>();
        messageRequestHandlers.add(messageRequestHandler);
        return messageRequestHandlers;
    }
}