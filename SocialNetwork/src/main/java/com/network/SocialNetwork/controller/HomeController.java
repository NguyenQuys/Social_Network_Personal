package com.network.SocialNetwork.controller;

import java.util.*;
import java.util.stream.Collectors;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.network.SocialNetwork.eenum.Status;
import com.network.SocialNetwork.entity.FriendBlock;
import com.network.SocialNetwork.entity.FriendRequest;
import com.network.SocialNetwork.entity.Group;
import com.network.SocialNetwork.entity.Notifications;
import com.network.SocialNetwork.entity.Post;
import com.network.SocialNetwork.entity.Statistic;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.FriendBlockRepository;
import com.network.SocialNetwork.repository.FriendRequestRepository;
import com.network.SocialNetwork.repository.NotificationRepository;
import com.network.SocialNetwork.repository.StatisticRepository;
import com.network.SocialNetwork.repository.UserRepository;
import com.network.SocialNetwork.service.GroupService;
import com.network.SocialNetwork.service.PostService;
import com.network.SocialNetwork.service.UserService;

@Controller
@RequestMapping
public class HomeController {
    // -------------------REPO------------------
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private FriendBlockRepository friendBlockRepository;

    @Autowired
    private StatisticRepository statisticRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    // ------------------SERVICE---------------------------
    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private PostService postService;

    public String GetUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                // Handle UserDetails (typically from username/password authentication)
                return ((UserDetails) principal).getUsername();
            } else if (principal instanceof OAuth2User) {
                // Handle OAuth2User (typically from OAuth2/OpenID Connect authentication)
                return ((OAuth2User) principal).getAttribute("email");
                // Replace "email" with the actual attribute you want to use (e.g., "name",
                // "preferred_username")
            } else {
                throw new IllegalStateException("Unknown principal type: " + principal.getClass());
            }
        } else {
            throw new IllegalStateException("User not authenticated");
        }
    }

    @GetMapping
public String home(Model model, @RequestParam(value = "message", required = false) String message) {
    if (message != null) {
        model.addAttribute("message", message);
    }

    List<Long> listIdFriendUser = new ArrayList<>();
    List<User> listInfoFriendCurrently = new ArrayList<>();

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()
            && !(authentication instanceof AnonymousAuthenticationToken)) {
        Optional<User> optionalUser;

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String email = oauth2User.getAttribute("email");
            optionalUser = userRepository.findByEmail(email);
        } else {
            optionalUser = userRepository.findByUsername(authentication.getName());
        }

        if (optionalUser.isPresent()) {
            User currentlyUser = optionalUser.get();
            Long idCurrentlyUser = currentlyUser.getId();

            Map<String, Object> attributes = new HashMap<>();
            User userChosen = optionalUser.get();
            attributes.put("userChosen", userChosen);
            userChosen.setActiveStatus(true);
            userRepository.save(userChosen);

            Statistic newsStatistic = new Statistic();
            newsStatistic.setVisitors(idCurrentlyUser);
            newsStatistic.setVisitAt(LocalDate.now());
            statisticRepository.save(newsStatistic);

            var getListFriend = friendRequestRepository.getListFriend(idCurrentlyUser);
            List<FriendBlock> blockList = friendBlockRepository.findAll();

            Set<Long> usersBlockedByCurrentUser = blockList.stream()
                    .filter(block -> block.getRequester().getId().equals(idCurrentlyUser))
                    .map(block -> block.getAddressee().getId())
                    .collect(Collectors.toSet());

            Set<Long> usersWhoBlockedCurrentUser = blockList.stream()
                    .filter(block -> block.getAddressee().getId().equals(idCurrentlyUser))
                    .map(block -> block.getRequester().getId())
                    .collect(Collectors.toSet());

            for (FriendRequest idFriendUser : getListFriend) {
                Long friendId;
                if (idFriendUser.getRequester().getId().equals(idCurrentlyUser)) {
                    friendId = idFriendUser.getAddressee().getId();
                } else {
                    friendId = idFriendUser.getRequester().getId();
                }
                if (!usersBlockedByCurrentUser.contains(friendId)
                        && !usersWhoBlockedCurrentUser.contains(friendId)) {
                    listIdFriendUser.add(friendId);
                }
            }

            for (Long friendId : listIdFriendUser) {
                Optional<User> friendUserOptional = userRepository.findById(friendId);
                friendUserOptional.ifPresent(listInfoFriendCurrently::add);
            }
            attributes.put("listInfoFriendCurrently", listInfoFriendCurrently);

            List<Post> sortedPosts;
            if (currentlyUser.getRole().getAuthority().equals("ADMIN")) {
                sortedPosts = postService.getAllPost()
                        .stream()
                        .filter(post -> post.getIsCensored())
                        .sorted(Comparator.comparing(Post::getTimestamp).reversed())
                        .collect(Collectors.toList());
            } else {
                sortedPosts = postService.getAllPost()
                        .stream()
                        .filter(post -> post.getIsCensored() &&
                                (listInfoFriendCurrently.stream()
                                .anyMatch(user -> user.getId().equals(post.getSender().getId())) ||
                                post.getSender().getId().equals(idCurrentlyUser)) &&
                                !usersBlockedByCurrentUser.contains(post.getSender().getId()) &&
                                !usersWhoBlockedCurrentUser.contains(post.getSender().getId()))
                        .sorted(Comparator.comparing(Post::getTimestamp).reversed())
                        .collect(Collectors.toList());
            }
            attributes.put("posts", sortedPosts);

            List<Post> postsLikedByCurrentUser = sortedPosts.stream()
                    .filter(post -> post.getLikedBy().stream()
                            .anyMatch(user -> user.getId().equals(idCurrentlyUser)))
                    .collect(Collectors.toList());
            attributes.put("postsLikedByCurrentUser", postsLikedByCurrentUser);

            model.addAllAttributes(attributes);

            return "users/like-fragment"; // ko trả về index là do lấy riêng div like vs comment ra để reload div đó
        }
    }
    return "redirect:/login";
}


    // Để xuất ra kết quả tìm kiếm
    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model) {
        String username = GetUserName();
        Optional<User> currentlyUserOpt = userRepository.findByUsername(username);
        currentlyUserOpt.ifPresent(value -> model.addAttribute("currentlyUser", value));

        model.addAttribute("query", query);

        if (currentlyUserOpt.isPresent()) {
            User currentlyUser = currentlyUserOpt.get();
            Long idCurrentlyUser = currentlyUser.getId();

            List<FriendBlock> friendBlockList = friendBlockRepository.findByAddressee(idCurrentlyUser);

            List<Long> blockedRequesterIds = friendBlockList.stream()
                    .map(friendBlock -> friendBlock.getRequester().getId())
                    .collect(Collectors.toList());

            List<User> userResults = userService.search(query);

            userResults.removeIf(user -> user.getId().equals(currentlyUser.getId()) ||
                    user.getRole().getName().equals("ADMIN") ||
                    blockedRequesterIds.contains(user.getId()));

            model.addAttribute("usersResult", userResults);

            List<Group> groupResults = groupService.search(query);

            model.addAttribute("groupsResult", groupResults);
        } else {
            model.addAttribute("usersResult", Collections.emptyList());
        }

        return "users/search-list-result";
    }

    // Để xuất ra kết quả tìm kiếm nhưng ghi đè lên ở trang Home
    @GetMapping("/search-override")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
        List<User> users = userService.search(keyword);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/add-friend-request-list")
    public String AddFriendRequestList(Model model) {
        Long idUserCurrently = null;
        // Navigation Bar
        String username = GetUserName();
        Optional<User> currentlyUserOpt = userRepository.findByUsername(username);
        if (currentlyUserOpt.isPresent()) {
            User user = currentlyUserOpt.get();
            idUserCurrently = user.getId();
            model.addAttribute("currentlyUser", user);
        }

        List<FriendRequest> friendRequestList = friendRequestRepository.findAllAddFriendRequestToUser(idUserCurrently);
        friendRequestList.removeIf(m -> m.getStatus().equals(Status.ACCEPTED));
        model.addAttribute("friendRequestList", friendRequestList);
        return "users/list-friend-request";
    }

    @GetMapping("/personal-account-settings")
    public String accountSettings(Model model)
    {
        // Lấy list group
        String username = GetUserName();
        User currentUser = userRepository.findByUsername(username).get();
        var listGroup = groupService.getListGroup(currentUser);
        model.addAttribute("listGroup", listGroup);

        return "account-settings";
    }

    // --------------------ERROR AREA STARTS------------------------------
    @GetMapping("/403_error")
    public String Error403() {
        return "errors/403_error";
    }

    @GetMapping("/not-found-error")
    public String NotFoundError() {
        return "errors/notFound_error";

    }
    // --------------------ERROR AREA ENDS------------------------------
}
