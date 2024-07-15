package com.network.SocialNetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.network.SocialNetwork.entity.FriendRequest;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> 
{
    @Query("SELECT fr FROM FriendRequest fr WHERE fr.requester.id = :requesterId AND fr.addressee.id = :addresseeId")
    FriendRequest findByRequesterAndAddressee(@Param("requesterId") Long requesterId, @Param("addresseeId") Long addresseeId);

    @Query("SELECT fr FROM FriendRequest fr WHERE fr.addressee.id = :addresseeId")
    List<FriendRequest> findAllAddFriendRequestToUser(@Param("addresseeId") Long addresseeId);

    @Query("SELECT fr FROM FriendRequest fr " +
    "WHERE (fr.addressee.id = :currentlyUserId OR fr.requester.id = :currentlyUserId) " +
    "AND fr.status = 'ACCEPTED'")
    List<FriendRequest> getListFriend(@Param("currentlyUserId") Long currentlyUserId);
}
