package com.dob.telegrambotformsocial.repository;

import com.dob.telegrambotformsocial.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByTelegramUserId(Long id);

    @Query(nativeQuery = true,
            value = "SELECT telegram_user_id FROM users")
    List<Long> findAllTelegramUserIds();
}
