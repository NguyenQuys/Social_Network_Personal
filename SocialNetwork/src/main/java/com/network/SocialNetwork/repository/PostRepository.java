package com.network.SocialNetwork.repository;

import com.network.SocialNetwork.entity.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.timestamp >= :startOfDay AND p.timestamp < :endOfDay")
    List<Post> findPostsByDate(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT DATE(p.timestamp), COUNT(p) FROM Post p WHERE p.timestamp BETWEEN :start AND :end GROUP BY DATE(p.timestamp)")
    List<Object[]> countPostsByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
