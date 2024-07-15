package com.network.SocialNetwork.entity;

import java.time.LocalDateTime;

import com.network.SocialNetwork.eenum.MessageType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long ChatId;

    private MessageType type;
    private String content;
    private String sender;
    private String recipient;

    private LocalDateTime timestamp;

}