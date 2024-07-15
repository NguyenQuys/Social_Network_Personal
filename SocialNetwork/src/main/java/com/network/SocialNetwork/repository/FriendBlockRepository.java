package com.network.SocialNetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.network.SocialNetwork.entity.FriendBlock;

@Repository
public interface FriendBlockRepository extends JpaRepository<FriendBlock,Long> {
    
    @Query("SELECT fb FROM FriendBlock fb WHERE fb.requester.id = :requesterId AND fb.addressee.id = :addresseeId")
    FriendBlock findByRequesterAndAddressee(@Param("requesterId") Long requesterId, @Param("addresseeId") Long addresseeId);

    @Query("SELECT fb FROM FriendBlock fb WHERE fb.addressee.id = :addresseeId")
    List<FriendBlock> findByAddressee(@Param("addresseeId") Long addresseeId);
}
