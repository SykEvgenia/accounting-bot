package com.github.zhenya.accountingbot.service.handler;

import com.github.zhenya.accountingbot.cache.Cache;
import com.github.zhenya.accountingbot.constant.Status;
import com.github.zhenya.accountingbot.entity.Account;
import com.github.zhenya.accountingbot.view.ViewComponent;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountEnterNameMessageHandlerTest {

    @Mock
    private Cache cache;
    @Mock
    private ViewComponent viewComponent;

    @InjectMocks
    private AccountEnterNameMessageHandler handler;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @Captor
    ArgumentCaptor<Status> statusCaptor;

    @Test
    void handleMessageRequest_expectSuccess() {
        when(cache.getAccount(123456L)).thenReturn(new Account());
        when(viewComponent.getEnterSumMessage(123456L)).thenReturn(new SendMessage(123456L, ""));

        SendMessage sendMessage = handler.handleMessageRequest("myAccount", 123456L);

        assertNotNull(sendMessage);
        verify(cache).getAccount(123456L);
        verify(viewComponent).getEnterSumMessage(123456L);
        verify(cache).putAccount(eq(123456L), accountCaptor.capture());
        assertEquals("myAccount", accountCaptor.getValue().getName());
        verify(cache).putStatus(eq(123456L), statusCaptor.capture());
        assertEquals(Status.ENTER_ACCOUNT_SUM, statusCaptor.getValue());
    }

    @Test
    void handleMessageRequest_expectErrorMessageWhenSumValueIsInvalid() {
        SendMessage sendMessage = handler.handleMessageRequest("myAccount", 123456L);

        assertNotNull(sendMessage);
        verify(cache).getAccount(123456L);
        verify(cache, never()).putAccount(anyLong(), any());
        verify(cache, never()).putStatus(anyLong(), any());
    }

    @Test
    void getStatus_expectSuccess() {
        assertEquals(Status.ENTER_ACCOUNT_NAME, handler.getStatus());
    }
}
