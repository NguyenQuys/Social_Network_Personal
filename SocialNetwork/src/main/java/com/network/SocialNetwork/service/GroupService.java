package com.network.SocialNetwork.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.network.SocialNetwork.entity.Group;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.GroupRepository;
import com.network.SocialNetwork.repository.UserRepository;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Group> search(String keyword) {
        return groupRepository.findByNameContainingIgnoreCase(keyword);
    }  

    public List<Group> getListGroup(User admin)
    {
        return groupRepository.findByAdmin(admin);
    }
}
