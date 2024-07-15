package com.network.SocialNetwork.service;

import com.network.SocialNetwork.entity.PasswordResetToken;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.PasswordResetTokenRepository;
import com.network.SocialNetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PasswordResetService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setToken(token);
        myToken.setUser(user);
        myToken.setExpiryDate(new Date(System.currentTimeMillis() + 3600000)); // 1 hour expiry
        tokenRepository.save(myToken);
    }

    public String validatePasswordResetToken(String token) {
        PasswordResetToken passToken = tokenRepository.findByToken(token);

        if (passToken == null) {
            return "invalidToken";
        }

        if (passToken.getExpiryDate().before(new Date())) {
            return "expired";
        }

        return null;
    }

    public User getUserByPasswordResetToken(String token) {
        return tokenRepository.findByToken(token).getUser();
    }

    public void sendResetTokenEmail(User user, String token) {
        String url = "http://localhost:8081/resetPassword?token=" + token;
        String message = "Click the link to reset your password: " + url;
        emailService.sendEmail(user.getEmail(), "Reset Password", message);
    }
}
