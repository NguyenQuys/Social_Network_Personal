package com.network.SocialNetwork.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
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

    private String elapsedTime;

    private int likes;

    @ManyToOne
    @JoinColumn()
    private User user; 
    
    private String authorName; 

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Video> videos;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
    private List<Comment> comments;



    public Post(String content, User user, String authorName, List<Image> images, List<Video> videos) {
        this.content = content;
        this.user = user;
        this.authorName = authorName;
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