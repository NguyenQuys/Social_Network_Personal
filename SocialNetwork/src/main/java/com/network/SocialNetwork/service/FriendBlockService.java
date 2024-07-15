package com.network.SocialNetwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.network.SocialNetwork.entity.FriendBlock;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.FriendBlockRepository;
import com.network.SocialNetwork.repository.UserRepository;

@Service
public class FriendBlockService {

    @Autowired
    private FriendBlockRepository friendBlockRepository;

    @Autowired
    private UserRepository userRepository;

    public FriendBlock blockAndUnblock(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("Receiver not found"));

        FriendBlock friendBlock = friendBlockRepository.findByRequesterAndAddressee(senderId, receiverId);
        if (friendBlock == null) {
            FriendBlock newFriendBlock = new FriendBlock();
            newFriendBlock.setRequester(sender);
            newFriendBlock.setAddressee(receiver);
            return friendBlockRepository.save(newFriendBlock);
        } else {
            friendBlockRepository.delete(friendBlock);
            return null; 
        }
    }

}
