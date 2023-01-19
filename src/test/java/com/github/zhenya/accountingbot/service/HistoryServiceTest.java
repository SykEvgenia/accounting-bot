package com.github.zhenya.accountingbot.service;

import com.github.zhenya.accountingbot.cache.Cache;
import com.github.zhenya.accountingbot.constant.Status;
import com.github.zhenya.accountingbot.entity.History;
import com.github.zhenya.accountingbot.repository.HistoryRepository;
import com.github.zhenya.accountingbot.view.ViewComponent;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static com.github.zhenya.accountingbot.constant.Status.ENTER_HISTORY_SUM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoryServiceTest {

    @Mock
    private Cache cache;
    @Mock
    private HistoryRepository historyRepository;
    @Mock
    private ViewComponent viewComponent;

    @InjectMocks
    private HistoryService service;

    @Captor
    ArgumentCaptor<Status> statusCaptor;

    @Captor
    ArgumentCaptor<History> historyCaptor;

    @Test
    void getHistories() {
        when(historyRepository.findAllByAccountId(12L)).thenReturn(Collections.emptyList());
        when(viewComponent.getHistories(anyList(), eq(123456L), eq(12L))).thenReturn(new SendMessage(123456L, ""));

        SendMessage sendMessage = service.getHistories(12L, 123456L);

        assertNotNull(sendMessage);
        verify(historyRepository).findAllByAccountId(12L);
        verify(viewComponent).getHistories(anyList(), eq(123456L), eq(12L));
    }

    @Test
    void createHistory() {
        when(viewComponent.getHistorySumMessage(123456L)).thenReturn(new SendMessage(123456L, ""));

        SendMessage sendMessage = service.createHistory(12L, 123456L);

        assertNotNull(sendMessage);
        verify(cache).putHistory(eq(123456L), historyCaptor.capture());
        assertEquals(12L, historyCaptor.getValue().getAccountId());
        verify(cache).putStatus(eq(123456L), statusCaptor.capture());
        assertEquals(ENTER_HISTORY_SUM, statusCaptor.getValue());
    }
}