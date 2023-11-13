package com.dob.telegrambotformsocial.service;

import com.dob.telegrambotformsocial.entity.User;

public interface MessageService {
    void saveMessage(String messageBot, String messageUser, Long userId);
}
