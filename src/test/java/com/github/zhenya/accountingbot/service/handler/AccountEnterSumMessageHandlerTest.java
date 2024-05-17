package com.github.zhenya.accountingbot.service.handler;

import com.github.zhenya.accountingbot.cache.Cache;
import com.github.zhenya.accountingbot.entity.Account;
import com.github.zhenya.accountingbot.repository.AccountRepository;
import com.github.zhenya.accountingbot.view.ViewComponent;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static com.github.zhenya.accountingbot.constant.Status.ENTER_ACCOUNT_SUM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountEnterSumMessageHandlerTest {

    @Mock
    private Cache cache;
    @Mock
    private ViewComponent viewComponent;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountEnterSumMessageHandler handler;
    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @Test
    void handleMessageRequest_expectSuccess() {
        when(cache.getAccount(123456L)).thenReturn(new Account());
        when(accountRepository.findAllByChatId(123456L)).thenReturn(new ArrayList<>());
        when(viewComponent.getSuccessCreatedAccountMessage(notNull(), eq(123456L))).thenReturn(new SendMessage(123456L, ""));

        SendMessage sendMessage = handler.handleMessageRequest("2334", 123456L);

        assertNotNull(sendMessage);
        verify(cache).getAccount(123456L);
        verify(cache).deleteStatus(123456L);
        verify(cache).deleteAccount(123456L);
        verify(accountRepository).findAllByChatId(123456L);
        verify(viewComponent).getSuccessCreatedAccountMessage(notNull(), eq(123456L));
        verify(accountRepository).save(accountCaptor.capture());
        assertEquals(2334L, accountCaptor.getValue().getSum());
    }

    @Test
    void handleMessageRequest_expectErrorMessageWhenSumValueIsInvalid() {
        handler.handleMessageRequest("myAccount", 123456L);

        verifyNoInteractions(cache);
        verifyNoInteractions(accountRepository);
    }

    @Test
    void handleMessageRequest_expectErrorMessageWhenAccountInCacheIsAbsent() {
        SendMessage sendMessage = handler.handleMessageRequest("2334", 123456L);

        assertNotNull(sendMessage);
        verify(cache).getAccount(123456L);
        verifyNoInteractions(accountRepository);
        verifyNoInteractions(viewComponent);
    }

    @Test
    void getStatus_expectSuccess() {
        assertEquals(ENTER_ACCOUNT_SUM, handler.getStatus());
    }
}
