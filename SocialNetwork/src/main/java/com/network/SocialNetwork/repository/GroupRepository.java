package com.network.SocialNetwork.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.network.SocialNetwork.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group,Long> {
    List<Group> findByNameContainingIgnoreCase(String keyword);
}
