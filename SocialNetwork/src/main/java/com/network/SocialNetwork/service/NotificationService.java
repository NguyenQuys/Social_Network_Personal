package com.network.SocialNetwork.service;

import com.network.SocialNetwork.eenum.Role;
import com.network.SocialNetwork.entity.*;
import com.network.SocialNetwork.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    public Notifications likeNotification(Long idPost, Long idSender) {
        Post post = postRepository.findById(idPost).orElseThrow(() -> new RuntimeException("Post not found"));
        User requester = userRepository.findById(idSender).orElseThrow(() -> new RuntimeException("User not found"));
        User addressee = userRepository.findById(post.getUser().getId()).orElseThrow(() -> new RuntimeException("User not found"));

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

    public Notifications markRead(Long notiId)
    {
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

    public Notifications addComment(Long idPost, Long idSender)
    {
        Post post = postRepository.findById(idPost).orElseThrow(() -> new RuntimeException("Post not found"));
        User requester = userRepository.findById(idSender).orElseThrow(() -> new RuntimeException("User not found"));

        Notifications newNotification = new Notifications();
        newNotification.setPost(post);
        newNotification.setRequester(requester);
        newNotification.setAddressee(post.getUser());
        newNotification.setType("COMMENT");
        newNotification.setContent(" đã bình luận bài viết của bạn!");
        newNotification.setCreated_at(LocalDateTime.now());
        newNotification.setIsRead(false);
        return notificationRepository.save(newNotification);
    }

    public void sendReportToAdmin(Long postId, String reason, Long userIdReport) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User userReport = userRepository.findById(userIdReport).orElseThrow(() -> new RuntimeException("User not found"));
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
}
