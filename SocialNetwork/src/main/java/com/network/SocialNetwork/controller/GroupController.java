package com.network.SocialNetwork.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.codehaus.groovy.runtime.StringGroovyMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.network.SocialNetwork.entity.Group;
import com.network.SocialNetwork.entity.GroupMembership;
import com.network.SocialNetwork.entity.Post;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.GroupMembershipRepository;
import com.network.SocialNetwork.repository.GroupRepository;
import com.network.SocialNetwork.repository.PostRepository;
import com.network.SocialNetwork.repository.UserRepository;
import com.network.SocialNetwork.service.GroupMembershipService;
import com.network.SocialNetwork.service.GroupService;
import com.network.SocialNetwork.service.NotificationService;
import com.network.SocialNetwork.service.PostService;
import com.network.SocialNetwork.service.UserService;

@Controller
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMembershipRepository groupMembershipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private GroupMembershipService groupMembershipService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

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

    @GetMapping("/{id}")
    public String groupProfile(@PathVariable Long id,
            @Param("notiId") Long notiId,
            Model model) {
        if (notiId != null) {
            notificationService.markRead(notiId);
        }

        Group groupChosen = groupRepository.findById(id).orElse(null);
        if (groupChosen == null) {
            return "redirect:/not-found-error";
        }
        model.addAttribute("groupChosen", groupChosen);

        List<Post> listPosts = postService.getAllPost().stream()
                .filter(post -> post.getGroupReceive() != null &&
                        post.getGroupReceive().getId().equals(id) &&
                        post.getIsCensored())
                .sorted(Comparator.comparing(Post::getTimestamp).reversed())
                .toList();

        model.addAttribute("listPosts", listPosts);

        List<Post> postsLikedByCurrentUser = listPosts.stream()
                .filter(post -> post.getLikedBy().stream()
                        .anyMatch(user -> user.getId().equals(getCurrentUser().get().getId())))
                .collect(Collectors.toList());
        model.addAttribute("postsLikedByCurrentUser", postsLikedByCurrentUser);

        var checkIfSentRequest = groupMembershipService.specificGroupMembership(groupChosen,
                getCurrentUser().orElse(null));
        model.addAttribute("checkIfSentRequest", checkIfSentRequest);

        return "group";
    }

    @PostMapping("/create")
    public String createNewGroup(@ModelAttribute Group group,
            RedirectAttributes redirectAttributes) {
        if (!group.getAvatarFile().isEmpty()) {
            String avatarUrl = saveFile(group.getAvatarFile(), "src/main/resources/static/user/assets/images/profile/");
            group.setAvatar(avatarUrl);
        }
        groupRepository.save(group);
        groupMembershipService.createGroup(group);

        redirectAttributes.addFlashAttribute("successMessage", "Tạo nhóm thành công");
        return "redirect:/personal-account-settings#create-group";
    }

    public String saveFile(MultipartFile file, String directory) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(directory + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return "/user/assets/images/profile/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @PostMapping("/send-request-to-join-group")
    public String joinGroup(@RequestParam("idGroup") Long idGroup,
            @RequestParam("idSender") Long idSender,
            RedirectAttributes redirectAttributes) {
        Group group = groupRepository.findById(idGroup).get();
        User sender = userRepository.findById(idSender).get();
        groupMembershipService.sendRequestToJoinGroup(group, sender);
        notificationService.sendRequestToAdminGroup(sender.getId(), group);

        redirectAttributes.addFlashAttribute("message", "Gửi yêu cầu thành công.");
        redirectAttributes.addFlashAttribute("subMessage", "Yêu cầu tham gia nhóm của bạn sẽ được admin xem xét.");
        return "redirect:/groups/" + idGroup;
    }

    @GetMapping("/list-request/{id}")
    public String listRequest(@PathVariable("id") Long id, // Id này là IdGroup
            @Param("notiId") Long notiId, // notiId la de danh dau da xem
            Model model) {
        if (notiId != null) {
            notificationService.markRead(notiId);
        }

        Group groupChosen = groupRepository.findById(id).get();
        model.addAttribute("groupChosen", groupChosen);

        var currentUser = getCurrentUser().get();
        if (!currentUser.getId().equals(groupChosen.getAdmin().getId())) {
            return "redirect:/403_error";
        }

        var listRequest = groupMembershipRepository.findAll().stream()
                .filter(group -> group.getGroup().getId() == id && group.isCensored() == false)
                .toList();

        var listUserRequestToJoin = listRequest.stream().map(GroupMembership::getUser).toList();
        model.addAttribute("listUserRequestToJoin", listUserRequestToJoin);
        return "group/request-list";
    }

    @PostMapping("/accept-request")
    public String acceptRequest(@RequestParam("idUserHasBeenAccepted") Long idUserHasBeenAccepted,
            @RequestParam("idGroup") Long idGroup,
            RedirectAttributes redirectAttributes) {
        User requester = userRepository.findById(idUserHasBeenAccepted).get();
        Group group = groupRepository.findById(idGroup).get();
        groupMembershipService.acceptRequest(requester, group);
        notificationService.notiForAcceptToRequester(requester, group);
        redirectAttributes.addFlashAttribute("message", "Chấp nhận thành công");
        return "redirect:/groups/list-request/" + idGroup;
    }

    @PostMapping("/reject-request")
    public String rejectRequest(
            @RequestParam("idSender") Long idSender,
            @RequestParam("idGroup") Long idGroup,
            @RequestParam("fromRoleUser") String fromRoleUser,
            RedirectAttributes redirectAttributes) {

        User sender = userRepository.findById(idSender)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + idSender));
        Group group = groupRepository.findById(idGroup)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group Id:" + idGroup));

        groupMembershipService.removeRequest(sender, group);

        if ("userJoint".equals(fromRoleUser)) {
            redirectAttributes.addFlashAttribute("message", "Hủy yêu cầu thành công");
            return "redirect:/groups/" + idGroup;
        } else if ("adminGroup".equals(fromRoleUser)) {
            redirectAttributes.addFlashAttribute("message", "Từ chối thành công");
            return "redirect:/groups/list-request/" + idGroup;
        } else if ("leaveGroup".equals(fromRoleUser)) {
            redirectAttributes.addFlashAttribute("message", "Rời nhóm thành công");
            return "redirect:/groups/" + idGroup;
        } else if ("adminRemoveMember".equals(fromRoleUser)) {
            redirectAttributes.addFlashAttribute("message", "Xóa thành viên thành công");
            return "redirect:/groups/" + idGroup + "/member";
        }
        return "redirect:/groups";
    }

    @GetMapping("/{idGroup}/review-list-post")
    public String reviewListPost(@PathVariable("idGroup") Long idGroup,
            @Param("notiId") Long notiId,
            Model model) {

        if (notiId != null) {
            notificationService.markRead(notiId);
        }

        List<Post> listReviewPost = postRepository.findAll().stream()
                .filter(post -> !post.getIsCensored() &&
                        post.getGroupReceive() != null &&
                        post.getGroupReceive().getId().equals(idGroup))
                .toList();

        Group group = groupRepository.findById(idGroup).get();
        model.addAttribute("groupChosen", group);
        model.addAttribute("listReviewPost", listReviewPost);
        return "group/review-list-post";
    }

    @PostMapping("/approve-post")
    public String approvePost(@RequestParam("idPost") Long idPost,
            @RequestParam("idGroup") Long idGroup,
            RedirectAttributes redirectAttributes) {
        Post post = postRepository.findById(idPost).get();
        Group group = groupRepository.findById(idGroup).get();
        postService.approvePost(post);
        notificationService.approvePost(group.getAdmin(), post.getSender(), post, group);
        redirectAttributes.addFlashAttribute("message", "Chấp nhận bài viết thành công!");
        return "redirect:/groups/" + idGroup + "/review-list-post";
    }

    @PostMapping("/reject-post")
    public String rejectPost(@RequestParam("idPost") Long idPost,
            @RequestParam("idGroup") Long idGroup,
            RedirectAttributes redirectAttributes) {
        postService.deletePostById(idPost);
        redirectAttributes.addFlashAttribute("message", "Từ chối bài viết thành công");
        return "redirect:/groups/" + idGroup + "/review-list-post";
    }

    @GetMapping("{idGroup}/member")
    public String memberGroup(@PathVariable("idGroup") Long idGroup, Model model) {
        Group group = groupRepository.findById(idGroup).get();
        model.addAttribute("groupChosen", group);

        var memberList = groupMembershipService.getMember(idGroup);
        model.addAttribute("memberList", memberList);
        return "group/member";
    }
}
