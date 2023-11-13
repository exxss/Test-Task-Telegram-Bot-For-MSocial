package com.dob.telegrambotformsocial.service.impl;



import com.dob.telegrambotformsocial.entity.User;
import com.dob.telegrambotformsocial.repository.UserRepository;
import com.dob.telegrambotformsocial.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findOrSaveUser(Update update) {
        var telegramUser = update.getMessage().getFrom();
        var appUserOpt = userRepository.findByTelegramUserId(telegramUser.getId());
        if (appUserOpt.isEmpty()) {
            User transientUser = User.builder()
                    .telegramUserId(telegramUser.getId())
                    .username(telegramUser.getUserName())
                    .lastMessageAt(LocalDateTime.now())
                    .build();
            return userRepository.save(transientUser);
        }
        return appUserOpt.get();
    }

    public void updateLastMessageAt(Long id) {
        User user = userRepository.findByTelegramUserId(id).orElse(null);
        if(user != null) {
        user.setLastMessageAt(LocalDateTime.now());
        userRepository.save(user);
        }
    }

    public List<Long> telegramUsersIds(){
        return userRepository.findAllTelegramUserIds();
    }
}
