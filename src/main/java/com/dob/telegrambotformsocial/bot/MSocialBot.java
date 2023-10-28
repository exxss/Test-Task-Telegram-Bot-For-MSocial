package com.dob.telegrambotformsocial.bot;


import com.dob.telegrambotformsocial.service.DailyDomainsService;
import com.dob.telegrambotformsocial.service.MessageService;
import com.dob.telegrambotformsocial.service.UserService;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Getter
@Component
public class MSocialBot extends TelegramLongPollingBot {

    private final UserService userService;
    private final MessageService messageService;
    private final DailyDomainsService dailyDomainsService;

    @Value("${bot.name}")
    private String username;

    public MSocialBot(@Value("${bot.token}") String token, UserService userService, MessageService messageService, DailyDomainsService dailyDomainsService) {
        super(token);
        this.userService = userService;
        this.messageService = messageService;
        this.dailyDomainsService = dailyDomainsService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        var user = userService.findOrSaveUser(update);
        String text = update.getMessage().getText();
        SendMessage message = new SendMessage();
        if (update.hasMessage() && update.getMessage().hasText()) {
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText("h");
        }
        if (text.equals("/cr")) {
            dailyDomainsService.deleteAllRows();
            dailyDomainsService.create();
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText("Записи показаны в консоле");

        }
        userService.updateLastMessageAt(user.getTelegramUserId());
        messageService.saveMessage(message.getText(),update.getMessage().getText(),user.getTelegramUserId());
        executeMessage(message);
    }

    @Override
    public String getBotUsername() {
        return username;
    }
    private void prepareAndSendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message);
    }

    @Scheduled(cron = "0 0 8 1/1 * ? *")
    private void sendAds(){
        List<Long> userIds = userService.telegramUsersIds();
        for (Long userId : userIds) {
          prepareAndSendMessage(userId,date() + ". Собрано " + dailyDomainsService.countDomains() + " доменов.");
        }
    }
    private void executeMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
//            log.error(ERROR_TEXT + e.getMessage());
        }
    }
    private String date(){
        LocalDateTime ldt = LocalDateTime.now().plusDays(1);
        DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        return formmat1.format(ldt);
    }
}