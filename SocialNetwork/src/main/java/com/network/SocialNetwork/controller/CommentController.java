package com.network.SocialNetwork.controller;

import com.network.SocialNetwork.entity.Comment;
import com.network.SocialNetwork.entity.Post;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.CommentRepository;
import com.network.SocialNetwork.repository.PostRepository;
import com.network.SocialNetwork.service.CommentService;
import com.network.SocialNetwork.service.NotificationService;
import com.network.SocialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class CommentController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private final CommentService commentService;

    public CommentController(UserService userService, CommentService commentService) {
        this.userService = userService;
        this.commentService = commentService;
    }

    private Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                // Handle UserDetails (typically from username/password authentication)
                String username = ((UserDetails) principal).getUsername();
                return userService.findByUsername(username);
            } else if (principal instanceof OAuth2User) {
                // Handle OAuth2User (typically from OAuth2/OpenID Connect authentication)
                String email = ((OAuth2User) principal).getAttribute("email");
                return userService.findByEmail(email);
            } else {
                throw new IllegalStateException("Unknown principal type: " + principal.getClass());
            }
        } else {
            throw new IllegalStateException("User not authenticated");
        }
    }

    @GetMapping("/addComment")
    @ResponseBody
    public String addComment(@RequestParam Long postId, @RequestParam String content, Model model) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> currentUser = getCurrentUser();
        if (postOptional.isPresent() && currentUser.isPresent()) {
            Post post = postOptional.get();
            Comment comment = commentService.createComment(currentUser, content, post);
            post.getComments().add(comment);
            postRepository.save(post);
            model.addAttribute("post", post);
            if (!currentUser.get().getId().equals(post.getSender().getId())) {
                notificationService.addComment(postId, currentUser.get().getId());
            }
            return content; // Return the comment content
        }
        return null;
    }

    


    @PostMapping("/delete-comment")
    public ResponseEntity<Map<String, String>> deleteComment(@RequestParam("commentId") Long commentId) {
        Map<String, String> response = new HashMap<>();
        Optional<Comment> comOptional = commentRepository.findById(commentId);
        if (comOptional.isPresent()) {
            commentRepository.delete(comOptional.get());
            response.put("status", "success");
            response.put("message", "Comment deleted successfully");
            return ResponseEntity.ok(response);
        }
        response.put("status", "error");
        response.put("message", "Comment not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
