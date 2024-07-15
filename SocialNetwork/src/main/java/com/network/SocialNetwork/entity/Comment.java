package com.network.SocialNetwork.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; 
    private String authorName; 
    private String content;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;


    public Comment(Long userId, String authorName, String content, Post post) {
        this.userId = userId;
        this.authorName = authorName;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.post = post;
    }

}