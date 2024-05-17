package com.github.zhenya.accountingbot.view;

import com.github.zhenya.accountingbot.entity.Account;
import com.github.zhenya.accountingbot.entity.History;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ViewComponent {

    public SendMessage getErrorMessage(Long chatId) {
        return new SendMessage(chatId, "Помилка, розпочніть створення спочатку");
    }

    public SendMessage getValueIntMessage(Long chatId) {
        return new SendMessage(chatId, "Значення має бути цифровим");
    }

    public SendMessage getAccountsMessage(Long chatId, List<Account> accounts) {
        String textMessage = accounts.isEmpty() ? "У вас ще немає рахунків" : "Ваші рахунки";
        return new SendMessage(chatId, textMessage)
                .replyMarkup(getAccountButtons(accounts));
    }

    public SendMessage getEnterSumMessage(Long chatId) {
        return new SendMessage(chatId, "Введіть суму");
    }

    public SendMessage getSuccessCreatedAccountMessage(List<Account> accounts, Long chatId) {
        return new SendMessage(chatId, "Рахунок успішно створений")
                .replyMarkup(getAccountButtons(accounts));
    }

    public SendMessage getEnterNameAccountMessage(Long chatId) {
        return new SendMessage(chatId, "Введіть назву рахунку");
    }

    public SendMessage getEnterCurrencyAccountMessage(Long chatId) {
        return new SendMessage(chatId, "Оберіть валюту рахунку")
                .replyMarkup(getCurrencyButtons());
    }

    public SendMessage getAccountMessage(Account account, Long chatId) {
        return new SendMessage(chatId, account.getName() + " " + account.getSum() + " " + account.getCurrency())
                .replyMarkup(getAccountInfoButtons(account));
    }

    private InlineKeyboardMarkup getAccountInfoButtons(Account account) {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{new InlineKeyboardButton("Переглянути всю історію").callbackData("GET /account/" + account.getId() + "/history")},
                new InlineKeyboardButton[]{new InlineKeyboardButton("Додати історію").callbackData("POST /account/" + account.getId() + "/history")},
                new InlineKeyboardButton[]{new InlineKeyboardButton("Видалити рахунок").callbackData("DELETE /account/" + account.getId())},
                new InlineKeyboardButton[]{new InlineKeyboardButton("Повернутись").callbackData("GET /account")}
        );
    }

    public SendMessage getHistories(List<History> histories, Long chatId, Long accountId) {
        return new SendMessage(chatId, "Ваша історія").replyMarkup(getHistoryButtons(histories, accountId));
    }

    public SendMessage getHistorySumMessage(Long chatId) {
        return new SendMessage(chatId, "Введіть суму (із знаком мінус якщо хочете відняти)");
    }

    public SendMessage getHistoryCommentMessage(Long chatId) {
        return new SendMessage(chatId, "Введіть коментар");
    }

    private InlineKeyboardMarkup getHistoryButtons(List<History> histories, Long accountId) {
        ArrayList<InlineKeyboardButton[]> buttons = new ArrayList<>();
        buttons.add(0, new InlineKeyboardButton[]{new InlineKeyboardButton("Повернутись").callbackData("GET /account/" + accountId)});
        histories.stream()
                .map(history -> new InlineKeyboardButton[]{new InlineKeyboardButton(history.getComment() + " " + history.getSum()).callbackData("no callbackData")})
                .forEach(buttons::add);
        return new InlineKeyboardMarkup(buttons.toArray(InlineKeyboardButton[][]::new));
    }

    private InlineKeyboardMarkup getCurrencyButtons() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton("USD").callbackData("PUT /account/currency/USD"),
                new InlineKeyboardButton("EUR").callbackData("PUT /account/currency/EUR"),
                new InlineKeyboardButton("UAN").callbackData("PUT /account/currency/UAN")
        );
    }

    private InlineKeyboardMarkup getAccountButtons(List<Account> accounts) {
        List<InlineKeyboardButton[]> buttons = accounts.stream()
                .map(account -> new InlineKeyboardButton[]{new InlineKeyboardButton(account.getName()
                        + " " + account.getSum() + " " + account.getCurrency()).callbackData("GET /account/" + account.getId())})
                .collect(Collectors.toList());
        buttons.add(getAccountCreationButton());
        return new InlineKeyboardMarkup(buttons.toArray(InlineKeyboardButton[][]::new));
    }

    private InlineKeyboardButton[] getAccountCreationButton() {
        return new InlineKeyboardButton[]{new InlineKeyboardButton("Створити рахунок").callbackData("POST /account")};
    }
}
