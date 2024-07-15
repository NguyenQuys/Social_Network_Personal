package com.network.SocialNetwork.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.network.SocialNetwork.entity.Notifications;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.NotificationRepository;
import com.network.SocialNetwork.repository.UserRepository;
import com.network.SocialNetwork.service.NotificationService;

@Controller
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public String GetUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                // Handle UserDetails (typically from username/password authentication)
                return ((UserDetails) principal).getUsername();
            } else if (principal instanceof OAuth2User) {
                // Handle OAuth2User (typically from OAuth2/OpenID Connect authentication)
                return ((OAuth2User) principal).getAttribute("email");
                // Replace "email" with the actual attribute you want to use (e.g., "name",
                // "preferred_username")
            } else {
                throw new IllegalStateException("Unknown principal type: " + principal.getClass());
            }
        } else {
            throw new IllegalStateException("User not authenticated");
        }
    }

    @PostMapping("/mark-all-as-read")
    public String markAllAsRead(Model model) {

        String username = GetUserName();
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User currentUser = optionalUser.get();
            model.addAttribute("notifications", notificationService.markAllAsRead(currentUser.getId()));
        }
        return "redirect:/";
    }

}
