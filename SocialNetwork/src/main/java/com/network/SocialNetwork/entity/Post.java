package com.network.SocialNetwork.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private LocalDateTime timestamp;
    private int likes;

    @ManyToOne
    @JoinColumn
    private User sender; 

    @ManyToOne
    @JoinColumn
    private User receiver;

    @ManyToOne
    @JoinColumn
    private Group groupReceive;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
    private List<Image> images = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
    private List<Video> videos = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
    private List<Notifications> notifications = new ArrayList<>();

    @Column(nullable = false)
    private Boolean isCensored = false;

    public Post(String content, User sender, User receiver, String authorName, List<Image> images, List<Video> videos) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = LocalDateTime.now();
        this.images = images;
        this.videos = videos;
    }

    @ManyToMany
    @JoinTable(
            name = "post_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likedBy = new HashSet<>();

    @Transient
    private boolean likedByCurrentUser;
}
