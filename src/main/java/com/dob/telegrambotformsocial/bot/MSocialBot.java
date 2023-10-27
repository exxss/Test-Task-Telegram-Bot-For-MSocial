package com.dob.telegrambotformsocial.bot;


import com.dob.telegrambotformsocial.service.DailyDomainsService;
import com.dob.telegrambotformsocial.service.MessageService;
import com.dob.telegrambotformsocial.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
        var appUser = userService.findOrSaveAppUser(update);
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
        if (text.equals("/get")) {
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText("Собрано "+ dailyDomainsService.countDomains() + " доменов");

        }
        userService.updateLastMessageAt(appUser.getTelegramUserId());
        messageService.saveMessage(message.getText(),update.getMessage().getText(),appUser.getTelegramUserId());
        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getBotUsername() {
        return username;
    }
}