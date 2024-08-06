package com.network.SocialNetwork.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Video(String url) {
        this.url = url;
    }
}
