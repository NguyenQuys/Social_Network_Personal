package com.network.SocialNetwork.controller;

import com.network.SocialNetwork.entity.PasswordResetToken;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.PasswordResetTokenRepository;
import com.network.SocialNetwork.repository.UserRepository;
import com.network.SocialNetwork.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.UUID;

@Controller
public class PasswordResetController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm() {
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String processForgotPasswordForm(@RequestParam("email") String email, Model model) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            model.addAttribute("error", "No user found with that email address");
            return "forgotPassword";
        }
        String token = UUID.randomUUID().toString();
        passwordResetService.createPasswordResetTokenForUser(user.get(), token);
        passwordResetService.sendResetTokenEmail(user.get(), token);

        model.addAttribute("message", "Check your email for a password reset link");
        return "forgotPassword";
    }

    @GetMapping("/resetPassword")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        String result = passwordResetService.validatePasswordResetToken(token);
        PasswordResetToken reset = passwordResetTokenRepository.findByToken(token);
        if (result != null) {
            model.addAttribute("error", result);
            return "redirect:/login";
        }
        model.addAttribute("email",reset.getUser().getEmail());
        model.addAttribute("token", token);
        return "resetPassword";
    }

    @PostMapping("/resetPassword")
    public String processResetPasswordForm(@RequestParam("token") String token,
                                           @RequestParam("password") String password, Model model) {
        String result = passwordResetService.validatePasswordResetToken(token);
        if (result != null) {
            model.addAttribute("error", result);
            return "resetPassword";
        }

        User user = passwordResetService.getUserByPasswordResetToken(token);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return "redirect:/login?resetSuccess";
    }
}
