package com.dob.telegrambotformsocial.service.impl;

import com.dob.telegrambotformsocial.entity.Message;
import com.dob.telegrambotformsocial.entity.User;
import com.dob.telegrambotformsocial.repository.MessageRepository;
import com.dob.telegrambotformsocial.repository.UserRepository;
import com.dob.telegrambotformsocial.service.DailyDomainsService;
import com.dob.telegrambotformsocial.service.MessageService;
import com.dob.telegrambotformsocial.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public void saveMessage(String messageBot,String messageUser,Long userId){
        User user = userRepository.findByTelegramUserId(userId).orElse(null);
        Message transientMessage = Message.builder()
                .messageTime(LocalDateTime.now())
                .messageUser(messageUser)
                .messageBot(messageBot)
                .user(user)
                .build();
        messageRepository.save(transientMessage);
    }
//    @Scheduled(cron = "0 0 8 * * ?")

}
