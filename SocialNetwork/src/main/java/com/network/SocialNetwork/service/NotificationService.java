package com.network.SocialNetwork.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.network.SocialNetwork.entity.Notifications;
import com.network.SocialNetwork.entity.Post;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.NotificationRepository;
import com.network.SocialNetwork.repository.PostRepository;
import com.network.SocialNetwork.repository.UserRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public Notifications likeNotification(Long idPost, Long idSender) {
        Post post = postRepository.findById(idPost).orElseThrow(() -> new RuntimeException("Post not found"));
        User requester = userRepository.findById(idSender).orElseThrow(() -> new RuntimeException("User not found"));

        Notifications newNotification = new Notifications();
        newNotification.setPost(post);
        newNotification.setRequester(requester);
        newNotification.setType("LIKE");
        newNotification.setContent(" đã thích bài viết của bạn!");
        newNotification.setCreated_at(LocalDateTime.now());
        newNotification.setIsRead(false);
        return notificationRepository.save(newNotification);
    }

    public void removeLikeNotifications(Long idPost, Long idSender) {
        Post post = postRepository.findById(idPost).orElseThrow(() -> new RuntimeException("Post not found"));
        User requester = userRepository.findById(idSender).orElseThrow(() -> new RuntimeException("User not found"));

        Notifications notification = notificationRepository
                .findByPostAndRequesterAndType(post, requester, "LIKE");
        notificationRepository.delete(notification);
    }

    public List<Notifications> markAllAsRead(Long currentIdUser) {
        List<Notifications> notifications = notificationRepository.findAll().stream()
                .filter(noti -> noti.getPost().getUser().getId().equals(currentIdUser))
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
        newNotification.setType("COMMENT");
        newNotification.setContent(" đã bình luận bài viết của bạn!");
        newNotification.setCreated_at(LocalDateTime.now());
        newNotification.setIsRead(false);
        return notificationRepository.save(newNotification);
    }

}
