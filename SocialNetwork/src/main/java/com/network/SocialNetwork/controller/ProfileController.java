package com.network.SocialNetwork.controller;

import com.network.SocialNetwork.eenum.Status;
import com.network.SocialNetwork.entity.FriendBlock;
import com.network.SocialNetwork.entity.FriendRequest;
import com.network.SocialNetwork.entity.Post;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.FriendBlockRepository;
import com.network.SocialNetwork.repository.FriendRequestRepository;
import com.network.SocialNetwork.repository.UserRepository;
import com.network.SocialNetwork.service.NotificationService;
import com.network.SocialNetwork.service.PostService;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.nio.file.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    // ---------------REPO------------------
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private FriendBlockRepository friendBlockRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private NotificationService notificationService;

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

    // Để chuyển đổi các tab about/ photos/ friends mà ko reload trang
    @GetMapping("/profileTab/{idUserChosen}")
    public String ProfileTab(@RequestParam(name = "tab", required = false, defaultValue = "tab1") String tab,
            Model model, @PathVariable Long idUserChosen) {
        Long idUsercurrently = null;
        List<Long> listIdFriendUser = new ArrayList<>();
        List<User> listInfoFriendUserChosen = new ArrayList<>();

        var getListFriend = friendRequestRepository.getListFriend(idUserChosen);
        // Lấy IdUserFriend
        for (FriendRequest idFriendUser : getListFriend) {
            if (idFriendUser.getRequester().getId().equals(idUserChosen)) {
                listIdFriendUser.add(idFriendUser.getAddressee().getId());
            } else {
                listIdFriendUser.add(idFriendUser.getRequester().getId());
            }
        }

        // Lấy thông tin User
        for (Long friendId : listIdFriendUser) {
            Optional<User> friendUserOptional = userRepository.findById(friendId);
            friendUserOptional.ifPresent(listInfoFriendUserChosen::add);
        }

        switch (tab) {
            case "timeline":
                return "profile :: contentFragment";
            case "about":
                Optional<User> userChosen = userRepository.findById(idUserChosen);
                userChosen.ifPresent(value -> model.addAttribute("userChosen", value));
                return "about :: contentFragment";
            case "photos":
                return "photos :: contentFragment";
            case "friends":
                model.addAttribute("listInfoFriendUserChosen", listInfoFriendUserChosen);
                return "friends :: contentFragment";
            default:
                return "profile :: contentFragment";
        }
    }

    @GetMapping("/{id}")
    public String UserProfile(@PathVariable Long id,
            @Param("notiId") Long notiId, // này lấy noti để trường hợp nhận thg báo sinh nhật
            Model model) {

        if (notiId != null) {
            notificationService.markRead(notiId);
        }
        Long idUser1Currently = null, idUserChosen = null;
        String usernameCurrently = GetUserName();
        Optional<User> currentlyUser = userRepository.findByUsername(usernameCurrently);
        if (currentlyUser.isPresent()) {
            User user1 = currentlyUser.get();
            idUser1Currently = user1.getId();
            model.addAttribute("currentlyUser", user1);
        }

        Optional<User> userChosen = userRepository.findById(id);
        if (userChosen.isPresent()) {
            User user2 = userChosen.get();
            idUserChosen = user2.getId();
            model.addAttribute("userChosen", user2);
        } else {
            return "redirect:/not-found-error";
        }

        FriendBlock friendBlockFindByURL = friendBlockRepository.findByRequesterAndAddressee(idUserChosen,
                idUser1Currently);
        if (friendBlockFindByURL != null) {
            return "redirect:/not-found-error";
        }

        FriendRequest friendRequest = null;
        friendRequest = friendRequestRepository.findByRequesterAndAddressee(idUser1Currently, idUserChosen);
        if (friendRequest == null) {
            friendRequest = friendRequestRepository.findByRequesterAndAddressee(idUserChosen, idUser1Currently);
        }

        if (friendRequest != null && friendRequest.getStatus().equals(Status.PENDING)) {
            model.addAttribute("checkIfExistInListFriendRequest", "pending");
        } else if (friendRequest != null && friendRequest.getStatus().equals(Status.ACCEPTED)) {
            model.addAttribute("checkIfExistInListFriendRequest", "accepted");
        } else {
            model.addAttribute("checkIfExistInListFriendRequest");
        }

        FriendBlock friendBlock = friendBlockRepository.findByRequesterAndAddressee(idUser1Currently, idUserChosen);
        if (friendBlock == null) {
            model.addAttribute("blockFunction", true);
        } else if (idUser1Currently.equals(friendBlock.getRequester().getId())) {
            model.addAttribute("blockFunction", false);
        }

        // Make idUser1Currently final to use in lambda
        final Long finalIdUser1Currently = idUser1Currently;
        final Long finalUserChosen = idUserChosen;

        List<Post> listPosts = postService.getAllPost()
                .stream()
                .filter(m -> m.getSender().getId().equals(finalUserChosen)
                        || m.getReceiver().getId().equals(finalUserChosen))
                .sorted(Comparator.comparing(Post::getTimestamp).reversed())
                .collect(Collectors.toList());

        model.addAttribute("listPosts", listPosts);

        List<Post> postsLikedByCurrentUser = listPosts.stream()
                .filter(post -> post.getLikedBy().stream()
                        .anyMatch(user -> user.getId().equals(finalIdUser1Currently)))
                .collect(Collectors.toList());

        model.addAttribute("postsLikedByCurrentUser", postsLikedByCurrentUser);

        // return "profile";
        return "users/like-fragment-profile";
    }

    @PostMapping("/{id}/update-image")
    public String updateImage(@PathVariable Long id,
            @RequestParam("avatarFile") MultipartFile avatarFile,
            @RequestParam("coverPhotoFile") MultipartFile coverPhotoFile,
            @RequestParam("bio") String bio,
            RedirectAttributes redirectAttributes) {

        Optional<User> currentlyUserOpt = userRepository.findById(id);
        if (!currentlyUserOpt.isPresent()) {
            return "redirect:/profile/" + id;
        }

        User currentlyUser = currentlyUserOpt.get();

        if (!avatarFile.isEmpty()) {
            String avatarUrl = saveFile(avatarFile, "src/main/resources/static/user/assets/images/profile/");
            currentlyUser.setAvatar(avatarUrl);
        }

        if (!coverPhotoFile.isEmpty()) {
            String coverPhotoUrl = saveFile(coverPhotoFile, "src/main/resources/static/user/assets/images/profile/");
            currentlyUser.setCoverPhoto(coverPhotoUrl);
        }

        currentlyUser.setBio(bio);

        userRepository.save(currentlyUser);
        redirectAttributes.addFlashAttribute("message", "Cập nhật trang cá nhân thành công!");
        return "redirect:/profile/" + id;
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

}
