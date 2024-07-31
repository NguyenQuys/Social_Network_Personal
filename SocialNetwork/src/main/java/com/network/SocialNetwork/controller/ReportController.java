package com.network.SocialNetwork.controller;

import java.util.Optional;

import org.hibernate.annotations.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.network.SocialNetwork.entity.Notifications;
import com.network.SocialNetwork.entity.Post;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.NotificationRepository;
import com.network.SocialNetwork.repository.PostRepository;
import com.network.SocialNetwork.repository.UserRepository;
import com.network.SocialNetwork.service.NotificationService;
import com.network.SocialNetwork.service.ReportService;
import com.network.SocialNetwork.service.UserService;


@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

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
    
    @PostMapping("/send-report")
    public String sendReport(@RequestParam("postId") Long postId,
                             @RequestParam("reason") String reason,
                             @RequestParam("userIdReport") Long userIdReport,
                             @Param("description") String description,
                             @RequestParam("sourcePage") String sourcePage,
                             RedirectAttributes redirectAttributes)
    {
        reportService.createReport(postId, reason, userIdReport, description);
        notificationService.sendReportToAdmin(postId, reason, userIdReport);

        redirectAttributes.addFlashAttribute("message","Gửi report thành công");
        if(sourcePage.equals("index"))
        {
            return "redirect:/";
        }
        else if(sourcePage.equals("post-detail"))
        {
            return "redirect:/post-detail/" + postId;
        }
        return null;
    }

    @PostMapping("/send-warning")
    public String sendWarning(@RequestParam("postId") Long postId,
                              @Param("notiId") Long notiId,
                               RedirectAttributes redirectAttributes)
    {
        Optional<Post> postOpt = postRepository.findById(postId);
        Optional<User> currentlyUserOpt = getCurrentUser();
        if (!currentlyUserOpt.isPresent()) {
            return "error";
        }
        User currentlyUser = currentlyUserOpt.get();

        if (!postOpt.isPresent()) {
            return "error";
        }
        Post postReported = postOpt.get();

        var newNotification = new Notifications();
        newNotification.setRequester(currentlyUser);
        newNotification.setAddressee(postReported.getSender());
        newNotification.setPost(postReported);
        newNotification.setType("CENSORSHIP");
        newNotification.setContent("Chúng tôi đã xem xét bài viết đã bị report của bạn.");

        notificationRepository.save(newNotification);
        redirectAttributes.addFlashAttribute("message","Gửi cảnh cáo thành công");
        return "redirect:/post-detail/" + postId;
    }
}
