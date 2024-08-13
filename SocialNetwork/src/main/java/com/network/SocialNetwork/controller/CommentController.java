package com.network.SocialNetwork.controller;

import com.network.SocialNetwork.entity.Comment;
import com.network.SocialNetwork.entity.Post;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.CommentRepository;
import com.network.SocialNetwork.repository.PostRepository;
import com.network.SocialNetwork.service.CommentService;
import com.network.SocialNetwork.service.NotificationService;
import com.network.SocialNetwork.service.UserService;

import jakarta.mail.Multipart;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    @PostMapping("/addComment")
    public String addComment(@RequestParam Long postId,
            @RequestParam String content,
            @RequestParam("imageComment") MultipartFile imageComment,
            Model model,
            RedirectAttributes redirectAttributes) {
        Post post = postRepository.findById(postId).orElse(null);
        User currentUser = getCurrentUser().get();
        Comment comment = commentService.createComment(currentUser, content, post);

        post.getComments().add(comment);
        if (!imageComment.isEmpty()) {
            String avatarUrl = saveFile(imageComment, "src/main/resources/static/user/assets/images/comment/");
            comment.setImage(avatarUrl);
        }
        postRepository.save(post);
        notificationService.notiPostRelevant(post, currentUser);

        redirectAttributes.addFlashAttribute("message", "Thêm bình luận thành công");
        return "redirect:/post-detail/" + postId; // Return the comment content

    }

    @PostMapping("/delete-comment")
    public String deleteComment(@RequestParam("commentId") Long commentId,
            @RequestParam("postId") Long postId,
            RedirectAttributes redirectAttributes) {
        commentRepository.deleteById(commentId);
        redirectAttributes.addFlashAttribute("message", "Xóa bình luận thành công");
        return "redirect:/post-detail/" + postId;
    }

    @PostMapping("/edit-comment")
    public String editComment(@RequestParam("commentId") Long commentId,
            @RequestParam("postId") Long postId,
            @RequestParam String content,
            @RequestParam("imageComment") MultipartFile imageComment,
            RedirectAttributes redirectAttributes) {
        // Find the existing comment
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID: " + commentId));

        // Update the comment content
        commentService.editCommentService(comment, content);

        // Check if a new image file is uploaded
        if (!imageComment.isEmpty()) {
            // Save the new image
            String imageUrl = saveFile(imageComment, "src/main/resources/static/user/assets/images/comment/");
            comment.setImage(imageUrl);
        }

        // Save the updated comment
        commentRepository.save(comment);

        // Add a success message
        redirectAttributes.addFlashAttribute("message", "Chỉnh sửa bình luận thành công");

        // Redirect back to the post detail page
        return "redirect:/post-detail/" + postId;
    }

    private String saveFile(MultipartFile file, String uploadDir) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            if (!file.isEmpty()) {
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                try (InputStream inputStream = file.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                    return "user/assets/images/comment/" + fileName; // Assuming you are storing images in the /images/
                                                                  // directory
                } catch (IOException e) {
                    throw new RuntimeException("Could not save file: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory: " + e.getMessage());
        }
        return null;
    }

}
