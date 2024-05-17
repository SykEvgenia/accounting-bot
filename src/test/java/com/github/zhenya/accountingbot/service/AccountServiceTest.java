package com.github.zhenya.accountingbot.service;

import com.github.zhenya.accountingbot.cache.Cache;
import com.github.zhenya.accountingbot.constant.Status;
import com.github.zhenya.accountingbot.entity.Account;
import com.github.zhenya.accountingbot.repository.AccountRepository;
import com.github.zhenya.accountingbot.view.ViewComponent;
import com.pengrad.telegrambot.request.SendMessage;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.github.zhenya.accountingbot.constant.Status.ENTER_ACCOUNT_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private Cache cache;
    @Mock
    private ViewComponent viewComponent;
    private final Account account = getAccount();
    @InjectMocks
    private AccountService service;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @Captor
    ArgumentCaptor<Status> statusCaptor;

    @Test
    void getAccounts() {
        when(accountRepository.findAllByChatId(123456L)).thenReturn(Collections.emptyList());
        when(viewComponent.getAccountsMessage(123456L, Collections.emptyList())).thenReturn(new SendMessage(123456L, ""));

        SendMessage sendMessage = service.getAccounts(123456L);

        assertNotNull(sendMessage);
        verify(accountRepository).findAllByChatId(123456L);
        verify(viewComponent).getAccountsMessage(eq(123456L), anyList());
    }

    @Test
    void createAccount() {
        when(viewComponent.getEnterCurrencyAccountMessage(123456L)).thenReturn(new SendMessage(123456L, ""));

        SendMessage sendMessage = service.createAccount(123456L);

        assertNotNull(sendMessage);
        verify(cache).putAccount(eq(123456L), accountCaptor.capture());
        assertEquals(123456L, accountCaptor.getValue().getChatId());
        verify(viewComponent).getEnterCurrencyAccountMessage(123456L);
    }

    @Test
    void fillAccountCurrency_expectSuccess() {
        when(cache.getAccount(123456L)).thenReturn(account);
        when(viewComponent.getEnterNameAccountMessage(123456L)).thenReturn(new SendMessage(123456L, ""));

        SendMessage sendMessage = service.fillAccountCurrency("UAN", 123456L);

        assertNotNull(sendMessage);
        verify(cache).getAccount(123456L);
        verify(cache).putStatus(eq(123456L), statusCaptor.capture());
        assertEquals(ENTER_ACCOUNT_NAME, statusCaptor.getValue());
        verify(cache).putAccount(eq(123456L), accountCaptor.capture());
        assertEquals("UAN", accountCaptor.getValue().getCurrency());
    }

    @Test
    void fillAccountCurrency() {
        SendMessage sendMessage = service.fillAccountCurrency("UAN", 123456L);

        assertNotNull(sendMessage);
        verify(cache).getAccount(123456L);
        verify(cache, never()).putAccount(anyLong(), any());
        verifyNoInteractions(viewComponent);
    }

    @Test
    void getAccountMessage() {
        when(accountRepository.findById(12L)).thenReturn(Optional.of(new Account()));
        when(viewComponent.getAccountMessage(any(), eq(123456L))).thenReturn(new SendMessage(123456L, ""));

        SendMessage sendMessage = service.getAccount(12L, 123456L);

        assertNotNull(sendMessage);
        verify(accountRepository).findById(12L);
        verify(viewComponent).getAccountMessage(any(), eq(123456L));
    }

    @Test
    void deleteAccount() {
        when(accountRepository.findAllByChatId(123456L)).thenReturn(new ArrayList<>());
        when(viewComponent.getAccountsMessage(eq(123456L), any())).thenReturn(new SendMessage(123456L, ""));

        SendMessage sendMessage = service.deleteAccount(12L, 123456L);

        assertNotNull(sendMessage);
        verify(accountRepository).deleteById(12L);
        verify(accountRepository).findAllByChatId(123456L);
        verify(viewComponent).getAccountsMessage(eq(123456L), any());
    }

    @NotNull
    private Account getAccount() {
        Account account = new Account();
        account.setCurrency("UAN");
        return account;
    }
}