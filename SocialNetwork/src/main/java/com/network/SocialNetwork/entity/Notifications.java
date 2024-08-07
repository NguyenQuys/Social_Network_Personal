package com.network.SocialNetwork.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne    
    @JoinColumn(nullable = false)
    private User addressee;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean isRead = false;

    @Column(nullable = false)
    private LocalDateTime created_at = LocalDateTime.now();
}


