package com.dob.telegrambotformsocial.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_MESSAGE_USER"))
    private User user;

    private LocalDateTime messageTime;

    private String messageBot;

    private String messageUser;
}
