package com.dob.telegrambotformsocial.bot;


import com.dob.telegrambotformsocial.service.DailyDomainsService;
import com.dob.telegrambotformsocial.service.MessageService;
import com.dob.telegrambotformsocial.service.UserService;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
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
        SendMessage message = new SendMessage();
        if (update.hasMessage() && update.getMessage().hasText()) {
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText("Ваше сообщение: " + update.getMessage().getText());
        }
        //todo обработка фото гс и видео
        //todo проверка всех функций
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
    @Scheduled(cron = "1 0 8 * * ?")
    private void deleteDomains(){
        System.out.println("delete");
        dailyDomainsService.deleteDomains();
    }

    @Scheduled(cron = "2 0 8 * * ?")
    private void saveDomains(){
        System.out.println("save");
        dailyDomainsService.saveDomains();
    }
    @Scheduled(cron = "3 0 8 * * ?")
    private void sendCountDomains(){
        String message = date() + ". Собрано " + dailyDomainsService.countDomains() + " доменов.";
        System.out.println("send");
        List<Long> userIds = userService.telegramUsersIds();
        for (Long userId : userIds) {
          prepareAndSendMessage(userId,message);
          messageService.saveMessage(message,"",userId);
        }
    }
    private void executeMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
    private String date(){
        LocalDateTime ldt = LocalDateTime.now().plusDays(1);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        return format.format(ldt);
    }
}