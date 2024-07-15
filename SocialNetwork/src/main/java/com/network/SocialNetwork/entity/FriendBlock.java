package com.network.SocialNetwork.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FriendBlock")
public class FriendBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long BlockFriendId;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne
    @JoinColumn(name = "addressee_id", nullable = false)
    private User addressee;
}
