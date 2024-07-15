package com.network.SocialNetwork.service;

import com.network.SocialNetwork.entity.Image;
import com.network.SocialNetwork.entity.Post;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.entity.Video;
import com.network.SocialNetwork.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PostService {
    //-----------------REPOSITORIES--------------------
    @Autowired
    private final PostRepository postRepository;
    //------------------------------------------------

    //-----------------SERVICES-----------------------
    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    //------------------------------------------------

    public Post savePost(String content, User user, List<Image> images, List<Video> videos) {
        Post post = new Post(content, user, user.getFullName(), images, videos);  // Pass the User object
        return postRepository.save(post);
    }
    

    public List<Post> getAllPost() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

    public List<Post> getPostsUploadedToday() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        return postRepository.findPostsByDate(startOfDay, endOfDay);
    }

    public Map<LocalDate, Long> getPostCountsLast7Days() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6); // 6 days ago + today = 7 days

        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        List<Object[]> results = postRepository.countPostsByDate(startOfDay, endOfDay);

        Map<LocalDate, Long> postCounts = new HashMap<>();

        for (Object[] result : results) {
            LocalDate date = ((java.sql.Date) result[0]).toLocalDate();
            Long count = (Long) result[1];
            postCounts.put(date, count);
        }

        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            postCounts.putIfAbsent(date, 0L);
        }

        return postCounts;
    }
}
