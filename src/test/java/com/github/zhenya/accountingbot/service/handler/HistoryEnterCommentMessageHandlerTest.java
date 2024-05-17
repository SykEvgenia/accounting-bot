package com.github.zhenya.accountingbot.service.handler;

import com.github.zhenya.accountingbot.cache.Cache;
import com.github.zhenya.accountingbot.entity.Account;
import com.github.zhenya.accountingbot.entity.History;
import com.github.zhenya.accountingbot.repository.AccountRepository;
import com.github.zhenya.accountingbot.repository.HistoryRepository;
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

import java.util.Optional;

import static com.github.zhenya.accountingbot.constant.Status.ENTER_HISTORY_COMMENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoryEnterCommentMessageHandlerTest {

    @Mock
    private Cache cache;
    @Mock
    private ViewComponent viewComponent;
    @Mock
    private HistoryRepository historyRepository;
    @Mock
    private AccountRepository accountRepository;
    private final History history = getHistory();
    private final Account account = getAccount();
    @InjectMocks
    private HistoryEnterCommentMessageHandler handler;

    @Captor
    private ArgumentCaptor<History> historyCaptor;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @Test
    void handleMessageRequest_expectSuccess() {
        when(cache.getHistory(123456L)).thenReturn(history);
        when(accountRepository.findById(123L)).thenReturn(Optional.of(account));
        when(viewComponent.getAccountMessage(notNull(), eq(123456L))).thenReturn(new SendMessage(123456L, ""));

        SendMessage sendMessage = handler.handleMessageRequest("myHistory", 123456L);

        assertNotNull(sendMessage);
        verify(cache).getHistory(123456L);
        verify(historyRepository).save(historyCaptor.capture());
        assertEquals("myHistory", historyCaptor.getValue().getComment());
        verify(accountRepository).findById(123L);
        verify(accountRepository).save(accountCaptor.capture());
        assertEquals(115L, accountCaptor.getValue().getSum());
        verify(viewComponent).getAccountMessage(notNull(), eq(123456L));
        verify(cache).deleteStatus(123456L);
        verify(cache).deleteHistory(123456L);
    }

    @Test
    void handleMessageRequest_expectErrorMessageWhenSumValueIsInvalid() {
        SendMessage sendMessage = handler.handleMessageRequest("myHistory", 123456L);

        assertNotNull(sendMessage);
        verify(cache).getHistory(123456L);
        verifyNoInteractions(historyRepository);
    }

    @Test
    void getStatus_expectSuccess() {
        assertEquals(ENTER_HISTORY_COMMENT, handler.getStatus());
    }

    @NotNull
    private Account getAccount() {
        Account account = new Account();
        account.setSum(80L);
        return account;
    }

    @NotNull
    private History getHistory() {
        History history = new History();
        history.setAccountId(123L);
        history.setSum(35L);
        return history;
    }
}
