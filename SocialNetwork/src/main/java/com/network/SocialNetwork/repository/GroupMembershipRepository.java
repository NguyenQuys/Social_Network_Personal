package com.network.SocialNetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.network.SocialNetwork.entity.Group;
import com.network.SocialNetwork.entity.GroupMembership;
import com.network.SocialNetwork.entity.User;

public interface GroupMembershipRepository extends JpaRepository<GroupMembership,Long>{
    GroupMembership findByGroupAndUserAndIsCensored(Group group,User user,Boolean isCensored);
    GroupMembership findByGroupAndUser(Group group,User user);
}
