package com.network.SocialNetwork.controller;

import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

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

    @GetMapping("/login")
    public String login() {
        return "users/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "users/login";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "users/login";
        }

        try {
            user.setCreated_at(LocalDate.now());
            userService.registerUser(user, passwordEncoder);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/login";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Đăng kí tài khoản thành công");
        return "redirect:/login";
    }

    @GetMapping("/AOLoginSuccess")
    public String oauth2LoginSuccessGG(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            User user = userService.processOAuthPostLogin(oauth2User, passwordEncoder);

            if (user == null) {
                // Xử lý khi userService trả về null
                model.addAttribute("errors", "Failed to process OAuth login");
                return "users/login"; // Hoặc redirect hoặc hiển thị thông báo lỗi tùy logic của bạn
            }
        }
        return "redirect:/";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {
        Optional<User> currentUserOpt = getCurrentUser();
        if (currentUserOpt.isPresent()) {
            User user = currentUserOpt.get();
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu cũ không đúng");
                return "redirect:/personal-account-settings#change-password";
            }

            if(oldPassword.equals(newPassword))
            {
                redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu mới và mật khẩu cũ phải khác nhau");
                return "redirect:/personal-account-settings#change-password";
            }

            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu mới và mật khẩu xác nhận không khớp");
                return "redirect:/personal-account-settings#change-password";
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật mật khẩu thành công");
            return "redirect:/personal-account-settings#change-password";
        }
        return "redirect:/error"; 
    }

    @PostMapping("/updateInfo")
    public String updateInfo(@ModelAttribute User updatedUser, RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = getCurrentUser();
            
        User currentUser = userOpt.get();
        currentUser.setFullName(updatedUser.getFullName());
        currentUser.setDateOfBirth(updatedUser.getDateOfBirth());
        currentUser.setGender(updatedUser.isGender());
        currentUser.setEmail(updatedUser.getEmail());
        currentUser.setPhone(updatedUser.getPhone());
    
        userService.saveButNotHash(currentUser);
    
        redirectAttributes.addFlashAttribute("successMessage", "Thông tin đã được cập nhật thành công.");
        return "redirect:/personal-account-settings#ChangeInfo";
    }
    

}
