package com.network.SocialNetwork.repository;

import com.network.SocialNetwork.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}