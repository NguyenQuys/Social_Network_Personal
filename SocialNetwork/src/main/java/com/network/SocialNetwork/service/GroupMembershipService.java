package com.network.SocialNetwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.network.SocialNetwork.entity.Group;
import com.network.SocialNetwork.entity.GroupMembership;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.GroupMembershipRepository;

@Service
public class GroupMembershipService {

    @Autowired
    private GroupMembershipRepository groupMembershipRepository;
    
    public GroupMembership createGroup(Group group)
    {
        GroupMembership newGroupMembership = new GroupMembership();
        newGroupMembership.setGroup(group);
        newGroupMembership.setUser(group.getAdmin());
        newGroupMembership.setCensored(true);
        return groupMembershipRepository.save(newGroupMembership);
    }

    public GroupMembership sendRequestToJoinGroup(Group group,User requester)
    {
        GroupMembership newGroupMembership = new GroupMembership();
        newGroupMembership.setGroup(group);
        newGroupMembership.setUser(requester);
        return groupMembershipRepository.save(newGroupMembership);
    }

    public GroupMembership acceptRequest(User user,Group group)
    {
        var request = groupMembershipRepository.findByGroupAndUserAndIsCensored(group,user,false);
        request.setCensored(true);
        return groupMembershipRepository.save(request);
    }

    public Boolean specificGroupMembership(Group group, User user) {
        var rowGroupMembership = groupMembershipRepository.findByGroupAndUser(group, user);
        if (rowGroupMembership == null) {
            return null; 
        }
        return rowGroupMembership.isCensored();
    }

    public void removeRequest(User idUserHasBeenReject,Group group)
    {
        var request = groupMembershipRepository.findByGroupAndUser(group, idUserHasBeenReject);
        groupMembershipRepository.delete(request);
    }
}
