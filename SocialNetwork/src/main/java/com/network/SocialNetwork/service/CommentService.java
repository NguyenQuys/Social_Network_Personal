package com.network.SocialNetwork.service;

import com.network.SocialNetwork.entity.Comment;
import com.network.SocialNetwork.entity.Post;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


    public Comment createComment(User user, String content, Post post) 
    {
        Comment newComment = new Comment();
        newComment.setUser(user);
        newComment.setContent(content);
        newComment.setPost(post);
        return commentRepository.save(newComment);
    }


    public Comment updateComment(Long commentId, String content) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setContent(content);
            return commentRepository.save(comment);
        }
        throw new RuntimeException("Comment not found with id " + commentId);
    }


    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public Comment editCommentService(Comment comment,String content)
    {
        comment.setContent(content);
        return commentRepository.save(comment);
    }


    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id " + commentId));
    }


    public List<Comment> getAllCommentsByPostId(Long postId) {
        return commentRepository.findAll()
                .stream()
                .filter(comment -> comment.getPost().getId().equals(postId))
                .collect(Collectors.toList());
    }
}
