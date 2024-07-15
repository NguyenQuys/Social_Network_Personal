package com.network.SocialNetwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.network.SocialNetwork.eenum.Status;
import com.network.SocialNetwork.entity.Comment;
import com.network.SocialNetwork.entity.FriendRequest;
import com.network.SocialNetwork.entity.Notifications;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.FriendRequestRepository;
import com.network.SocialNetwork.repository.NotificationRepository;
import com.network.SocialNetwork.repository.UserRepository;

import org.springframework.ui.Model;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            Optional<User> optionalUser;

            if (authentication.getPrincipal() instanceof OAuth2User) {
                OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                String email = oauth2User.getAttribute("email");
                optionalUser = userRepository.findByEmail(email);
            } else {
                String username = ((UserDetails) authentication.getPrincipal()).getUsername();
                optionalUser = userRepository.findByUsername(username);
            }

            if (optionalUser.isPresent()) {
                User currentlyUser = optionalUser.get();
                Long idCurrentlyUser = currentlyUser.getId();

                // Để lấy friend request list
                List<FriendRequest> friendRequestList = friendRequestRepository
                        .findAllAddFriendRequestToUser(idCurrentlyUser);
                friendRequestList
                        .removeIf(m -> m.getStatus().equals(Status.ACCEPTED) || m.getStatus().equals(Status.REJECTED));
                model.addAttribute("amountFriendRequests", friendRequestList.size());

                // Thêm notifications
                List<Notifications> notifications = notificationRepository.findAll().stream()
                        .filter(noti -> noti.getPost().getUser().getId().equals(idCurrentlyUser))
                        .sorted(Comparator.comparing(Notifications::getCreated_at).reversed())
                        .collect(Collectors.toList());
                model.addAttribute("notifications", notifications);

                // Sô lượng thông báo chua doc
                Long notificationsUnread = notificationRepository.findAll().stream()
                        .filter(noti -> noti.getPost().getUser().getId().equals(idCurrentlyUser) &&
                                !noti.getIsRead())
                        .count();

                model.addAttribute("notificationsUnread", notificationsUnread);

            }
        }
    }
}
