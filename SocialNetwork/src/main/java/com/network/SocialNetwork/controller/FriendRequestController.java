package com.network.SocialNetwork.controller;

import java.util.Optional;

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

import com.network.SocialNetwork.entity.FriendRequest;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.FriendRequestRepository;
import com.network.SocialNetwork.repository.UserRepository;
import com.network.SocialNetwork.service.FriendBlockService;
import com.network.SocialNetwork.service.FriendRequestService;

@Controller
@RequestMapping("/add-friend")

public class FriendRequestController {
    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendBlockService friendBlockService;

    public String GetUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                // Handle UserDetails (typically from username/password authentication)
                return ((UserDetails) principal).getUsername();
            } else if (principal instanceof OAuth2User) {
                // Handle OAuth2User (typically from OAuth2/OpenID Connect authentication)
                return ((OAuth2User) principal).getAttribute("email");
                // Replace "email" with the actual attribute you want to use (e.g., "name", "preferred_username")
            } else {
                throw new IllegalStateException("Unknown principal type: " + principal.getClass());
            }
        } else {
            throw new IllegalStateException("User not authenticated");
        }
    }

    @PostMapping("/send-request")
    public String sendRequest(@RequestParam("requesterId") Long requesterId,
                              @RequestParam("addresseeId") Long addresseeId,
                              RedirectAttributes redirectAttributes) {
        FriendRequest friendRequest = friendRequestService.sendFriendRequest(requesterId, addresseeId);
        if (friendRequest != null) {
            redirectAttributes.addFlashAttribute("message","Gửi lời mời kết bạn thành công");
            return "redirect:/profile/" + addresseeId + "?success=true";
        } else {
            return "redirect:/profile/" + addresseeId + "?success=false";
        }
    }

    @PostMapping("/cancel-request")
    public String CancelRequest(@RequestParam("requesterId") Long requesterId,
                                @RequestParam("addresseeId") Long addresseeId,
                                RedirectAttributes redirectAttributes) {
        FriendRequest friendRequest = friendRequestRepository.findByRequesterAndAddressee(requesterId, addresseeId);
        friendRequestRepository.delete(friendRequest);
        redirectAttributes.addFlashAttribute("message","Hủy lời mời kết bạn thành công");
        return "redirect:/profile/" + addresseeId;
    }

    @PostMapping("/accept-request")
    public String AcceptRequest(@Param("idUserHasBeenAccepted") Long idUserHasBeenAccepted,
                                RedirectAttributes redirectAttributes) {
        Long idCurrentlyUser = null;
        String username = GetUserName();
        Optional<User> currentlyUserOpt = userRepository.findByUsername(username);
        if (currentlyUserOpt.isPresent()) {
            User user = currentlyUserOpt.get();
            idCurrentlyUser = user.getId();
        }

        friendRequestService.AcceptFriendRequest(idUserHasBeenAccepted, idCurrentlyUser);
        redirectAttributes.addFlashAttribute("message", "Chúc mừng, bạn đã có thêm bạn 😍😍");
        return "redirect:/add-friend-request-list";
    }

    @PostMapping("/unfriend")
    public String Unfriend(@Param("idUserHasBeenUnfriend") Long idUserHasBeenUnfriend,
                            RedirectAttributes redirectAttributes) {
        Long idCurrentlyUser = null;
        String username = GetUserName();
        Optional<User> currentlyUserOpt = userRepository.findByUsername(username);
        if (currentlyUserOpt.isPresent()) {
            User user = currentlyUserOpt.get();
            idCurrentlyUser = user.getId();
        }

        FriendRequest friendship = friendRequestRepository.findByRequesterAndAddressee(idUserHasBeenUnfriend,
                idCurrentlyUser);
        if (friendship == null) {
            friendship = friendRequestRepository.findByRequesterAndAddressee(idCurrentlyUser, idUserHasBeenUnfriend);
        }
        friendRequestRepository.delete(friendship);
        redirectAttributes.addFlashAttribute("message","Unfriend thành công");
        return "redirect:/profile/" + idUserHasBeenUnfriend;
    }

    @PostMapping("/reject-request")
    public String RejectAddFriendRequest(@Param("idUserHasBeenReject") Long idUserHasBeenReject,
                                        RedirectAttributes redirectAttributes) {
        Long idCurrentlyUser = null;
        String username = GetUserName();
        Optional<User> currentlyUserOpt = userRepository.findByUsername(username);
        if (currentlyUserOpt.isPresent()) {
            User user = currentlyUserOpt.get();
            idCurrentlyUser = user.getId();
        }

        FriendRequest friendRequest = friendRequestRepository.findByRequesterAndAddressee(idUserHasBeenReject,
                idCurrentlyUser);
        friendRequestRepository.delete(friendRequest);
        redirectAttributes.addFlashAttribute("message", "Từ chối thành công!");
        return "redirect:/add-friend-request-list";
    }

    @PostMapping("/block-and-unblock")
    public String BlockAndUnblockFriend(@Param("idUserBlocked") Long idUserBlocked,
                                        RedirectAttributes redirectAttributes)
    {
        Long idCurrentlyUser = null;
        String username = GetUserName();
        Optional<User> currentlyUserOpt = userRepository.findByUsername(username);
        if (currentlyUserOpt.isPresent()) {
            User user = currentlyUserOpt.get();
            idCurrentlyUser = user.getId();
        }

        friendBlockService.blockAndUnblock(idCurrentlyUser, idUserBlocked);
        redirectAttributes.addFlashAttribute("message","Thay đổi thành công");
        return "redirect:/profile/" + idUserBlocked;
    }
    
}
