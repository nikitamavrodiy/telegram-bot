package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class BotService implements UpdatesListener {
    @Autowired
    TelegramBot telegramBot;
    @Autowired
    NotificationService notificationService;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.stream()
                .filter(update -> update.message() != null)
                .filter(update -> update.message().text() != null)
                .forEach(this::processUpdate);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processUpdate(Update update) {
        String userMessage = update.message().text();
        Long chatId = update.message().chat().id();
        if (userMessage.equals("/start")) {
            this.telegramBot.execute(
                    new SendMessage(chatId, "Привет! Я могу напомнить тебе о каком-то событии"));
        } else {
            if (this.notificationService.processNotification(chatId, userMessage)) {
                this.telegramBot.execute(new SendMessage(chatId, "Напоминалка создана!"));
            } else {
                this.telegramBot.execute(
                        new SendMessage(
                                chatId,
                                "Я принимаю сообщения в формате '01.01.2022 20:00 Сделать домашнюю работу'"));
            }
        }
    }

}
