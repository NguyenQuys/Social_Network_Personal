package com.network.SocialNetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.network.SocialNetwork.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long>{
    List<ChatMessage> findBySenderOrRecipient(String sender, String recipient);
    boolean existsBySenderAndRecipient(String sender, String recipient);
}
