package com.dob.telegrambotformsocial.service;



import com.dob.telegrambotformsocial.entity.User;
import com.dob.telegrambotformsocial.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;


import java.time.LocalDateTime;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findOrSaveAppUser(Update update) {
        var telegramUser = update.getMessage().getFrom();
        var appUserOpt = userRepository.findByTelegramUserId(telegramUser.getId());
        if (appUserOpt.isEmpty()) {
            User transientAppUser = User.builder()
                    .telegramUserId(telegramUser.getId())
                    .username(telegramUser.getUserName())
                    .lastMessageAt(LocalDateTime.now())
                    .build();
            return userRepository.save(transientAppUser);
        }
        return appUserOpt.get();
    }

    public void updateLastMessageAt(Long id, LocalDateTime time) {
        User user = userRepository.findUserByTelegramUserId(id);
        user.setLastMessageAt(time);
        userRepository.save(user);
    }
}
