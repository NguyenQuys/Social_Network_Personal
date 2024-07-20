package com.network.SocialNetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.network.SocialNetwork.entity.Notifications;
import com.network.SocialNetwork.entity.Post;
import com.network.SocialNetwork.entity.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications,Long>
{
    Notifications findByPostAndRequesterAndType(Post post, User requester, String type);
    Notifications findByPost(Post post);
    List<Notifications> findByPostId(Long postId);

}
