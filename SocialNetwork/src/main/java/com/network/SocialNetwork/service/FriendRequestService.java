package com.network.SocialNetwork.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.network.SocialNetwork.eenum.Status;
import com.network.SocialNetwork.entity.FriendRequest;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.FriendRequestRepository;
import com.network.SocialNetwork.repository.UserRepository;

@Service
public class FriendRequestService 
{    
    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserRepository userRepository;

    public FriendRequest sendFriendRequest(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("Receiver not found"));

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setRequester(sender);
        friendRequest.setAddressee(receiver);
        friendRequest.setStatus(Status.PENDING);
        friendRequest.setRequestedAt(new Timestamp(System.currentTimeMillis()));

        return friendRequestRepository.save(friendRequest);
    }

    public FriendRequest AcceptFriendRequest(Long senderId,Long receiverId)
    {
        FriendRequest friendRequest = friendRequestRepository.findByRequesterAndAddressee(senderId, receiverId);
        friendRequest.setAcceptedAt(new Timestamp(System.currentTimeMillis()));
        friendRequest.setStatus(Status.ACCEPTED);
        return friendRequestRepository.save(friendRequest);
    }

    public void UnfriendAndReject(Long senderId,Long receiverId)
    {
        FriendRequest friend = friendRequestRepository.findByRequesterAndAddressee(receiverId, senderId);
        friendRequestRepository.delete(friend);
    }



    
}
