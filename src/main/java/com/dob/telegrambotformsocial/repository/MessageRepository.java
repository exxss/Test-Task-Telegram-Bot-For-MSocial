package com.dob.telegrambotformsocial.repository;

import com.dob.telegrambotformsocial.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {

}
