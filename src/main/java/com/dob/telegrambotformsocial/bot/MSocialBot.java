package com.dob.telegrambotformsocial.bot;


import com.dob.telegrambotformsocial.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Getter
@Component
public class MSocialBot extends TelegramLongPollingBot {

    private final UserService userService;

    @Value("${bot.name}")
    private String username;

    public MSocialBot(@Value("${bot.token}") String token, UserService userService) {
        super(token);
        this.userService = userService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        var appUser = userService.findOrSaveAppUser(update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText("h");
            Date date = new Date(TimeUnit.SECONDS.toMillis(update.getMessage().getDate()));
            LocalDateTime time = Instant.ofEpochMilli(date.getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            userService.updateLastMessageAt(appUser.getTelegramUserId(),time);
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }
}