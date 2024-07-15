package com.network.SocialNetwork.service;

import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.network.SocialNetwork.eenum.Role;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.RoleRepository;
import com.network.SocialNetwork.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void save(@NotNull User user, PasswordEncoder passwordEncoder) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        com.network.SocialNetwork.entity.Role userRole = roleRepository.findRoleById(Role.USER.value);
        user.setRole(userRole);
        userRepository.save(user);
    }

    public void saveButNotHash(User user) {
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(!user.isAccountNonExpired())
                .accountLocked(!user.isAccountNonLocked())
                .credentialsExpired(!user.isCredentialsNonExpired())
                .disabled(!user.isEnabled())
                .build();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> search(String keyword) {
        return userRepository.findByFullNameContainingIgnoreCase(keyword);
    }

    public User registerUser(User user, PasswordEncoder passwordEncoder) throws Exception {
        if (findByUsername(user.getUsername()).isPresent()) {
            throw new Exception("Username đã tồn tại");
        }
        if (findByEmail(user.getEmail()).isPresent()) {
            throw new Exception("Email đã tồn tại");
        }
        if (findByPhone(user.getPhone()).isPresent()) {
            throw new Exception("Phone number đã tồn tại");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        com.network.SocialNetwork.entity.Role userRole = roleRepository.findRoleById(Role.USER.value);
        user.setRole(userRole);
        return userRepository.save(user);
    }

    public User processOAuthPostLogin(OAuth2User oauth2User,  PasswordEncoder passwordEncoder) {
        String email = oauth2User.getAttribute("email");
        Optional<User> existUser = userRepository.findByEmail(email);

        User user;
        if (existUser.isPresent()) {
            user = existUser.get();
        } else {
            user = new User();
            user.setEmail(email);
            user.setUsername(email); // Đây có thể sẽ cần thay đổi tùy vào logic của ứng dụng
            user.setFullName(oauth2User.getAttribute("name")); // Đây có thể là thông tin khác từ OAuth2
            user.setDateOfBirth(LocalDate.now());
            user.setCreated_at(LocalDate.now());// có thể cần lấy thông tin ngày sinh từ OAuth2 (nếu có)
            user.setPhone(null); // Để trống hoặc có thể cần lấy thông tin số điện thoại từ OAuth2
            user.setPassword(passwordEncoder.encode("")); //  có thể cần thiết lập mật khẩu mặc định hoặc tạo một token an toàn
            com.network.SocialNetwork.entity.Role userRole = roleRepository.findRoleById(Role.USER.value);
            user.setRole(userRole);
            userRepository.save(user);
        }
        return user;
    }
}
