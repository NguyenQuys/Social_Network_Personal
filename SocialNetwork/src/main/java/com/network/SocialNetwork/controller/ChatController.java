package com.network.SocialNetwork.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.network.SocialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.network.SocialNetwork.entity.ChatMessage;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.ChatMessageRepository;
import com.network.SocialNetwork.repository.UserRepository;

@Controller
public class ChatController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                // Handle UserDetails (typically from username/password authentication)
                String username = ((UserDetails) principal).getUsername();
                return userService.findByUsername(username);
            } else if (principal instanceof OAuth2User) {
                // Handle OAuth2User (typically from OAuth2/OpenID Connect authentication)
                String email = ((OAuth2User) principal).getAttribute("email");
                return userService.findByEmail(email);
            } else {
                throw new IllegalStateException("Unknown principal type: " + principal.getClass());
            }
        } else {
            throw new IllegalStateException("User not authenticated");
        }
    }

    @GetMapping("/chat")
    public String chatIndex(Model model) {
        Optional<User> currentlyUser = getCurrentUser();
        currentlyUser.ifPresent(value -> model.addAttribute("currentlyUser", value));
        return "chat";
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @GetMapping("/history")
    @ResponseBody
    public List<ChatMessage> getChatHistory(@RequestParam("username") String username) {
        String currentUsername = getCurrentUser().map(User::getUsername).orElseThrow(() -> new IllegalStateException("User not authenticated"));

        // Check if the current user is either the sender or recipient
        if (chatMessageRepository.existsBySenderAndRecipient(username, currentUsername) ||
                chatMessageRepository.existsBySenderAndRecipient(currentUsername, username)) {
            return chatMessageRepository.findBySenderOrRecipient(currentUsername, currentUsername);
        }

        // Return an empty list or throw an exception if not found
        return List.of(); // Or throw an exception
    }

}
