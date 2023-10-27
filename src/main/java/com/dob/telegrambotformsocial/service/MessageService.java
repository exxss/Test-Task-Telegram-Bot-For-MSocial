package com.dob.telegrambotformsocial.service;

import com.dob.telegrambotformsocial.entity.Message;
import com.dob.telegrambotformsocial.entity.User;
import com.dob.telegrambotformsocial.repository.MessageRepository;
import com.dob.telegrambotformsocial.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }
    public void saveMessage(String messageBot,String messageUser,Long userId){
        User user = userRepository.findUserByTelegramUserId(userId);
        Message transientMessage = Message.builder()
                .messageTime(LocalDateTime.now())
                .messageUser(messageUser)
                .messageBot(messageBot)
                .user(user)
                .build();
        messageRepository.save(transientMessage);
    }
}
