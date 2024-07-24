package com.network.SocialNetwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.network.SocialNetwork.eenum.Status;
import com.network.SocialNetwork.entity.Comment;
import com.network.SocialNetwork.entity.FriendBlock;
import com.network.SocialNetwork.entity.FriendRequest;
import com.network.SocialNetwork.entity.Notifications;
import com.network.SocialNetwork.entity.Post;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.FriendBlockRepository;
import com.network.SocialNetwork.repository.FriendRequestRepository;
import com.network.SocialNetwork.repository.NotificationRepository;
import com.network.SocialNetwork.repository.UserRepository;
import com.network.SocialNetwork.service.NotificationService;
import com.network.SocialNetwork.service.PostService;
import com.network.SocialNetwork.service.UserService;

import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    FriendBlockRepository friendBlockRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    private Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
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

    @ModelAttribute
    public void addGlobalAttributes(Model model) {

        List<User> listInfoFriendCurrently = new ArrayList<>();
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

                model.addAttribute("currentlyUser", currentlyUser);

                // Để lấy friend request list
                List<FriendRequest> friendRequestList = friendRequestRepository
                        .findAllAddFriendRequestToUser(idCurrentlyUser);
                friendRequestList
                        .removeIf(m -> m.getStatus().equals(Status.ACCEPTED) || m.getStatus().equals(Status.REJECTED));
                model.addAttribute("amountFriendRequests", friendRequestList.size());

                // Kiểm tra ngày sinh nhật
                List<User> userAndFriendsBirthday = new ArrayList<>();

                if (currentlyUser.getDateOfBirth().equals(LocalDate.now())) {
                    userAndFriendsBirthday.add(currentlyUser);
                }

                List<FriendRequest> getListFriend = friendRequestRepository.getListFriend(idCurrentlyUser);

                for (FriendRequest friend : getListFriend) {
                    User friendUser;
                    if (friend.getAddressee().getId().equals(idCurrentlyUser)) {
                        friendUser = friend.getRequester();
                    } else {
                        friendUser = friend.getAddressee();
                    }

                    if (friendUser.getDateOfBirth().equals(LocalDate.now())) {
                        userAndFriendsBirthday.add(friendUser);
                    }
                }

                if (userAndFriendsBirthday.size() != 0) {
                    notificationService.notiBirthday(userAndFriendsBirthday);
                }

                // Thêm notifications
                List<Notifications> notifications = notificationRepository.findAll().stream()
                        .filter(noti -> noti.getAddressee().getId().equals(idCurrentlyUser))
                        .sorted(Comparator.comparing(Notifications::getCreated_at).reversed())
                        .collect(Collectors.toList());
                model.addAttribute("notifications", notifications);

                // Sô lượng thông báo chua doc
                Long notificationsUnread = notificationRepository.findAll().stream()
                        .filter(noti -> noti.getAddressee().getId().equals(idCurrentlyUser) &&
                                !noti.getIsRead())
                        .count();

                model.addAttribute("notificationsUnread", notificationsUnread);

            }
        }
    }
}
