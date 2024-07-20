package com.network.SocialNetwork.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.network.SocialNetwork.entity.Image;
import com.network.SocialNetwork.entity.Notifications;
import com.network.SocialNetwork.entity.Post;
import com.network.SocialNetwork.entity.Report;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.entity.Video;
import com.network.SocialNetwork.repository.NotificationRepository;
import com.network.SocialNetwork.repository.PostRepository;
import com.network.SocialNetwork.repository.ReportRepository;
import com.network.SocialNetwork.repository.UserRepository;
import com.network.SocialNetwork.service.NotificationService;
import com.network.SocialNetwork.service.PostService;
import com.network.SocialNetwork.service.UserService;

@Controller
public class PostController {
    // ---------------REPOSITORIES START-----------------
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired ReportRepository reportRepository;
    // ---------------REPOSITORIES END-----------------

    // ---------------SERVICES START--------------------
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private NotificationService notificationService;
    // ---------------SERVICES END--------------------

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

    @PostMapping("/post")
    public String shareContent(@RequestParam String content,
            @RequestParam("images") List<MultipartFile> imageFiles,
            @RequestParam("videos") List<MultipartFile> videoFiles,
            RedirectAttributes redirectAttributes,
            Model model) {

        Optional<User> currentlyUseroOptional = getCurrentUser();
        if (!currentlyUseroOptional.isPresent()) {
            return "error";
        }
        User currentlyUser = currentlyUseroOptional.get();

        List<Image> images = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            if (imageFile.getOriginalFilename() == "") {
                continue;
            } else {
                String imageUrl = saveFile(imageFile, "src/main/resources/static/user/assets/images/post/");
                images.add(new Image(imageUrl));
            }
        }

        List<Video> videos = new ArrayList<>();
        for (MultipartFile videoFile : videoFiles) {
            if (videoFile.getOriginalFilename() == "") {
                continue;
            } else {
                String videoUrl = saveFile(videoFile, "src/main/resources/static/user/assets/images/post/");
                videos.add(new Video(videoUrl));
            }
        }

        // LocalDateTime currentTime = LocalDateTime.now();
        postService.savePost(content, currentlyUser, images, videos);
        redirectAttributes.addFlashAttribute("message", "Thêm bài viết thành công!");
        return "redirect:/";
    }

    @GetMapping("/post-detail/{postId}")
    public String postDetail(Model model,
            @PathVariable("postId") Long postId,
            @Param("notiId") Long notiId) {
        Optional<User> currentlyUserOptional = getCurrentUser();
        if (currentlyUserOptional.isEmpty()) {
            return "redirect:/error"; 
        }

        User currentlyUser = currentlyUserOptional.get();
        Post post = postRepository.findById(postId).orElse(null);

        if (post == null) {
            return "redirect:/error"; 
        }

        if(notiId != null)
        {
            notificationService.markRead(notiId);
        }

        boolean isPostLikedByCurrentUser = post.getLikedBy().stream()
                .anyMatch(user -> user.getId().equals(currentlyUser.getId()));

        model.addAttribute("isPostLikedByCurrentUser", isPostLikedByCurrentUser);
        model.addAttribute("postChosen", post);
        model.addAttribute("currentlyUser", currentlyUser);

        return "users/like-fragment-one-post";
    }

    @PostMapping("/edit-post")
    public String editPost(@RequestParam Long postId,
            @RequestParam String content,
            @RequestParam("images") List<MultipartFile> imageFiles,
            @RequestParam("videos") List<MultipartFile> videoFiles,
            RedirectAttributes redirectAttributes) {

        Optional<User> currentlyUser = getCurrentUser();
        if (!currentlyUser.isPresent()) {
            return "redirect:/login";
        }

        Optional<Post> optionalPost = postRepository.findById(postId);
        if (!optionalPost.isPresent()) {
            return "redirect:/";
        }

        Post post = optionalPost.get();

        post.setContent(content);
        post.setTimestamp(LocalDateTime.now());

        post.getImages().clear();
        for (MultipartFile imageFile : imageFiles) {
            if (imageFile.getOriginalFilename() == "") {
                continue;
            } else {
                String imageUrl = saveFile(imageFile, "src/main/resources/static/user/assets/images/post/");
                post.getImages().add(new Image(imageUrl));
            }
        }

        post.getVideos().clear();
        for (MultipartFile videoFile : videoFiles) {
            if (videoFile.getOriginalFilename() == "") {
                continue;
            } else {
                String videoUrl = saveFile(videoFile, "src/main/resources/static/user/assets/images/post/");
                post.getVideos().add(new Video(videoUrl));
            }
        }

        postRepository.save(post);
        redirectAttributes.addFlashAttribute("message", "Cập nhật bài đăng thành công!");

        return "redirect:/";
    }

    @GetMapping("/likePost")
    public String likePost(@RequestParam("postId") Long postId, Model model) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> currentUserOptional = getCurrentUser();

        if (postOptional.isPresent() && currentUserOptional.isPresent()) {
            Post post = postOptional.get();
            User currentUser = currentUserOptional.get();

            if (post.getLikedBy().contains(currentUser)) {
                post.getLikedBy().remove(currentUser);
                post.setLikes(post.getLikes() - 1);
                if (currentUser.getId() != post.getUser().getId()) {
                    notificationService.removeLikeNotifications(postId, currentUser.getId());
                }
            } else {
                post.getLikedBy().add(currentUser);
                post.setLikes(post.getLikes() + 1);
                if (currentUser.getId() != post.getUser().getId()) {
                    notificationService.likeNotification(postId, currentUser.getId());
                }
            }
            postRepository.save(post);
            model.addAttribute("post", post);
        }
        return "users/like-fragment :: likeContent";
    }

    @GetMapping("/likePostProfile")
    public String likePostProfile(@RequestParam("postId") Long postId, Model model) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> currentUserOptional = getCurrentUser();

        if (postOptional.isPresent() && currentUserOptional.isPresent()) {
            Post post = postOptional.get();
            User currentUser = currentUserOptional.get();

            if (post.getLikedBy().contains(currentUser)) {
                post.getLikedBy().remove(currentUser);
                post.setLikes(post.getLikes() - 1);
                if (currentUser.getId() != post.getUser().getId()) {
                    notificationService.removeLikeNotifications(postId, currentUser.getId());
                }
            } else {
                post.getLikedBy().add(currentUser);
                post.setLikes(post.getLikes() + 1);
                if (currentUser.getId() != post.getUser().getId()) {
                    notificationService.likeNotification(postId, currentUser.getId());
                }
            }
            postRepository.save(post);
            model.addAttribute("post", post);
        }
        return "users/like-fragment-profile :: likeContent";
    }

    @GetMapping("/likeChosenPost")
    public String likeChosenPost(@RequestParam("postId") Long postId, Model model) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> currentUserOptional = getCurrentUser();

        if (postOptional.isPresent() && currentUserOptional.isPresent()) {
            Post post = postOptional.get();
            User currentUser = currentUserOptional.get();

            if (post.getLikedBy().contains(currentUser)) {
                post.getLikedBy().remove(currentUser);
                post.setLikes(post.getLikes() - 1);
                if (currentUser.getId() != post.getUser().getId()) {
                    notificationService.removeLikeNotifications(postId, currentUser.getId());
                }
            } else {
                post.getLikedBy().add(currentUser);
                post.setLikes(post.getLikes() + 1);
                if (currentUser.getId() != post.getUser().getId()) {
                    notificationService.likeNotification(postId, currentUser.getId());
                }
            }
            postRepository.save(post);
            model.addAttribute("post", post);
        }
        return "users/like-fragment-one-post :: likeContent";
    }

    @PostMapping("/delete-post")
    public String deletePost(@RequestParam("postId") Long postId, RedirectAttributes redirectAttributes) {
        Optional<User> currentlyUserOpt = getCurrentUser();
        Optional<Post> postOptional = postRepository.findById(postId);
    
        if (!currentlyUserOpt.isPresent() || !postOptional.isPresent()) {
            return "error"; // or some appropriate error page
        }
    
        User currentlyUser = currentlyUserOpt.get();
        Post post = postOptional.get();
    
        // Delete associated reports
        List<Report> reports = reportRepository.findByPostId(postId);
        reportRepository.deleteAll(reports);
    
        // Delete associated notifications
        List<Notifications> notifications = notificationRepository.findByPostId(postId);
        notificationRepository.deleteAll(notifications);
    
        if (!currentlyUser.getRole().getId().equals(1L)) {
            postRepository.deleteById(postId);
        } else {
            Notifications newNotifications = new Notifications();
            newNotifications.setRequester(currentlyUser);
            newNotifications.setAddressee(post.getUser());
            newNotifications.setType("DELETE POST");
            newNotifications.setContent("Bài viết của bạn đã bị kiểm duyệt và bị xóa bởi Admin");
    
            notificationRepository.save(newNotifications);
    
            postRepository.deleteById(postId);
        }
    
        redirectAttributes.addFlashAttribute("message", "Xóa bài viết thành công!");
        return "redirect:/";
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
                    return "user/assets/images/post/" + fileName; // Assuming you are storing images in the /images/
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

    private String calculateElapsedTime(LocalDateTime currentTime, LocalDateTime postTime) {
        Duration duration = Duration.between(postTime, currentTime);
        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return seconds + " seconds ago";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + " minutes ago";
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + " hours ago";
        } else {
            long days = seconds / 86400;
            return days + " days ago";
        }
    }
}
