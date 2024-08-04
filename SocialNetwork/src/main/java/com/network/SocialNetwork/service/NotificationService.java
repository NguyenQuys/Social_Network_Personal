package com.network.SocialNetwork.service;

import com.network.SocialNetwork.eenum.Role;
import com.network.SocialNetwork.entity.*;
import com.network.SocialNetwork.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

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

    public Notifications likeNotification(Long idPost, Long idSender) {
        Post post = postRepository.findById(idPost).orElseThrow(() -> new RuntimeException("Post not found"));
        User requester = userRepository.findById(idSender).orElseThrow(() -> new RuntimeException("User not found"));
        User addressee = userRepository.findById(post.getSender().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notifications newNotification = new Notifications();
        newNotification.setRequester(requester);
        newNotification.setType("LIKE");
        newNotification.setPost(post);
        newNotification.setContent(" đã thích bài viết của bạn!");
        newNotification.setCreated_at(LocalDateTime.now());
        newNotification.setAddressee(addressee);
        return notificationRepository.save(newNotification);
    }

    public void removeLikeNotifications(Long idPost, Long idSender) {
        Post post = postRepository.findById(idPost).orElseThrow(() -> new RuntimeException("Post not found"));
        User requester = userRepository.findById(idSender).orElseThrow(() -> new RuntimeException("User not found"));

        Notifications notification = notificationRepository
                .findByPostAndRequesterAndType(post, requester, "LIKE");
        notificationRepository.delete(notification);
    }

    public Notifications markRead(Long notiId) {
        Notifications notification = notificationRepository.findById(notiId).orElse(null);
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }

    public List<Notifications> markAllAsRead(Long currentIdUser) {
        List<Notifications> notifications = notificationRepository.findAll().stream()
                .filter(noti -> noti.getAddressee().getId().equals(currentIdUser))
                .collect(Collectors.toList());

        notifications.forEach(noti -> noti.setIsRead(true));
        return notificationRepository.saveAll(notifications);
    }

    public Notifications addComment(Long idPost, Long idSender) {
        Post post = postRepository.findById(idPost).orElseThrow(() -> new RuntimeException("Post not found"));
        User requester = userRepository.findById(idSender).orElseThrow(() -> new RuntimeException("User not found"));

        Notifications newNotification = new Notifications();
        newNotification.setPost(post);
        newNotification.setRequester(requester);
        newNotification.setAddressee(post.getSender());
        newNotification.setType("COMMENT");
        newNotification.setContent(" đã bình luận bài viết của bạn!");
        newNotification.setCreated_at(LocalDateTime.now());
        newNotification.setIsRead(false);
        return notificationRepository.save(newNotification);
    }

    public void notiPostRelevant(Post post, User userComment) {
        User sender = post.getSender();
        User receiver = post.getReceiver();
    
        // Check if receiver is null
        if (receiver != null) {
            // If the commenter is the sender
            if (userComment.getId().equals(sender.getId())) {
                // Do nothing or handle if needed
            }
            // If the commenter is the receiver
            else if (userComment.getId().equals(receiver.getId())) {
                // Do nothing or handle if needed
            }
            // If the commenter is a third party
            else {
                createAndSaveNotification(post, userComment, sender);
                createAndSaveNotification(post, userComment, receiver);
            }
        } else {
            Group groupReceive = post.getGroupReceive();
            if (groupReceive != null) {
                // Notify all members of the group
                for (GroupMembership membership : groupReceive.getGroupMemberships()) {
                    User member = membership.getUser();
                    // Only notify the members if the commenter is a third party
                    if (!userComment.getId().equals(sender.getId())) {
                        createAndSaveNotification(post, userComment, sender);
                    }
                }
            }
        }
    }
    
    private void createAndSaveNotification(Post post, User requester, User addressee) {
        Notifications newNoti = new Notifications();
        newNoti.setPost(post);
        newNoti.setRequester(requester);
        newNoti.setAddressee(addressee);
        newNoti.setType("COMMENT");
        
        if (addressee.getId().equals(post.getSender().getId())) {
            newNoti.setContent(requester.getFullName() + " đã bình luận bài viết của bạn!");
        } else if (addressee.getId().equals(post.getReceiver().getId())) {
            newNoti.setContent(requester.getFullName() + " đã bình luận về một bài viết liên quan đến bạn!");
        } else {
            newNoti.setContent(requester.getFullName() + " đã bình luận về một bài viết!");
        }
        
        newNoti.setCreated_at(LocalDateTime.now());
        newNoti.setIsRead(false);
        notificationRepository.save(newNoti);
    }
    
    

    public Notifications notiBirthday(List<User> usersHaveBirthday) {
        User currentUser = getCurrentUser().orElseThrow(() -> new IllegalStateException("User not authenticated"));

        Notifications newNotification = new Notifications();
        newNotification.setType("BIRTHDAY");

        if (usersHaveBirthday.size() == 1) {
            User user = usersHaveBirthday.get(0);
            newNotification.setRequester(user);
            if (user.equals(currentUser)) {
                newNotification.setAddressee(user);
                newNotification.setContent("Hôm nay là ngày sinh nhật của bạn. Chúc bạn sinh nhật vui vẻ 🎂🎂");
            } else {
                newNotification.setAddressee(currentUser);
                newNotification.setContent(
                        "Hôm nay là sinh nhật của " + user.getFullName() + ". Hãy chúc mừng nhật bạn ấy thôi nào 🎂🎂");
            }
        } else {
            newNotification.setRequester(usersHaveBirthday.get(0));
            newNotification.setAddressee(currentUser);
            for (User user : usersHaveBirthday) {
                if (user.equals(currentUser)) {
                    newNotification.setContent("Hôm nay là sinh nhật của bạn và " + (usersHaveBirthday.size() - 1)
                            + " người khác. Chúc mừng sinh nhật 🎂🎂");
                    break;
                } else {
                    newNotification.setContent("Hôm nay là sinh nhật của " + usersHaveBirthday.size()
                            + " người bạn. Chúc mừng sinh nhật họ 🎂🎂");
                }
            }
        }

        boolean exists = notificationRepository.existsByTypeAndAddresseeAndContent(
                newNotification.getType(), newNotification.getAddressee(), newNotification.getContent());

        if (!exists) {
            return notificationRepository.save(newNotification);
        } else {
            return null;
        }
    }

    public Notifications postOnSBProfile(User sender, User receiver, Post post) {
        Notifications newNoti = new Notifications();
        newNoti.setRequester(sender);
        newNoti.setAddressee(receiver);
        newNoti.setPost(post);
        newNoti.setType("POST");
        newNoti.setContent(sender.getFullName() + " đã đăng bài viết lên trang cá nhân của bạn");
        return notificationRepository.save(newNoti);
    }

    public void sendReportToAdmin(Long postId, String reason, Long userIdReport) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User userReport = userRepository.findById(userIdReport)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<User> adminUsers = userRepository.findUsersByRoleId(1L);

        if (adminUsers.isEmpty()) {
            throw new RuntimeException("Admin users not found");
        }

        for (User admin : adminUsers) {
            Notifications newNotification = new Notifications();
            newNotification.setRequester(userReport);
            newNotification.setPost(post);
            newNotification.setType(reason);
            newNotification.setContent(" đã báo cáo 1 bài viết có liên quan đến " + reason);
            newNotification.setAddressee(admin);
            notificationRepository.save(newNotification);
        }
    }

    public Notifications acceptFriendRequestNoti(Long idUserSendRequest, Long idAccepter) {
        User requester = userRepository.findById(idUserSendRequest).get();
        User accepter = userRepository.findById(idAccepter).get();

        Notifications newNoti = new Notifications();
        newNoti.setRequester(accepter);
        newNoti.setAddressee(requester);
        newNoti.setType("ACCEPT FRIENDREQUEST");
        newNoti.setContent(accepter.getFullName() + " đã chấp nhận lời mời kết bạn của bạn");
        return notificationRepository.save(newNoti);
    }

    public Notifications sendRequestToAdminGroup(Long idSender, Group group) {
        User sender = userRepository.findById(idSender).get();

        Notifications newNoti = new Notifications();
        newNoti.setRequester(sender);
        newNoti.setAddressee(group.getAdmin());
        newNoti.setType("REQUEST_TO_JOIN_GROUP");
        newNoti.setContent("đã gửi yêu cầu tham gia nhóm " + group.getName());
        newNoti.setGroup(group);
        return notificationRepository.save(newNoti);
    }

    public Notifications notiForAcceptToRequester(User requester, Group group) // function này để thông báo khi cha
    {
        Notifications newNoti = new Notifications();
        newNoti.setRequester(group.getAdmin());
        newNoti.setAddressee(requester);
        newNoti.setGroup(group);
        newNoti.setType("ACCEPTED_JOINING_GROUP");
        newNoti.setContent("Chúc mừng! Bạn đã là thành viên của nhóm " + group.getName() + "😍😍");
        return notificationRepository.save(newNoti);
    }

    public Notifications sendRequestToPost(User sender, User receiver, Post post, Group group) {
        Notifications newNoti = new Notifications();
        newNoti.setRequester(sender);
        newNoti.setAddressee(receiver);
        newNoti.setPost(post);
        newNoti.setType("REVIEW_POST");
        newNoti.setContent("đã gửi yêu cầu đăng bài trong nhóm " + group.getName());
        newNoti.setGroup(group);
        return notificationRepository.save(newNoti);
    }

    public Notifications approvePost(User sender, User receiver, Post post, Group group) {
        Notifications newNoti = new Notifications();
        newNoti.setRequester(sender);
        newNoti.setAddressee(receiver);
        newNoti.setPost(post);
        newNoti.setGroup(group);
        newNoti.setType("APPROVE_POST");
        newNoti.setContent("Bài viết của bạn đã được chấp nhận");
        return notificationRepository.save(newNoti);
    }

    public boolean existsById(Long notiId) {
        return notificationRepository.existsById(notiId);
    }

}
