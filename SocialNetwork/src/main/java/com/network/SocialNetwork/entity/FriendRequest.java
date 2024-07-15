package com.network.SocialNetwork.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

import com.network.SocialNetwork.eenum.Status;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FriendRequests")
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendRequest_id")
    private Long friendRequestId;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne
    @JoinColumn(name = "addressee_id", nullable = false)
    private User addressee;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('PENDING', 'ACCEPTED', 'REJECTED') DEFAULT 'PENDING'")
    private Status status;

    @Column(name = "requested_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp requestedAt;

    @Column(name = "accepted_at")
    private Timestamp acceptedAt;
}

