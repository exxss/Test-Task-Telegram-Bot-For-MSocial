package com.dob.telegrambotformsocial.service;

import com.dob.telegrambotformsocial.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface UserService {
    User findOrSaveUser(Update update);
    void updateLastMessageAt(Long id);
    List<Long> telegramUsersIds();

}
