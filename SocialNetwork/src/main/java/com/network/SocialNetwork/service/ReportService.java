package com.network.SocialNetwork.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.network.SocialNetwork.entity.Post;
import com.network.SocialNetwork.entity.Report;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.PostRepository;
import com.network.SocialNetwork.repository.ReportRepository;
import com.network.SocialNetwork.repository.UserRepository;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public Report createReport(Long postId, String reason, Long userIdReport, String description) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + postId));
        User reporter = userRepository.findById(userIdReport).orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userIdReport));

        Report newReport = new Report();
        newReport.setPost(post);
        newReport.setReporter(reporter);
        newReport.setReportedType(reason);
        newReport.setDescription(description);
        
        return reportRepository.save(newReport);
    }
}
