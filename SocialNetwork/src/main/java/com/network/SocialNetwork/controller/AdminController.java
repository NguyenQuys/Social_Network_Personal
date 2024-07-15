package com.network.SocialNetwork.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.network.SocialNetwork.entity.Post;
import com.network.SocialNetwork.entity.Statistic;
import com.network.SocialNetwork.entity.User;
import com.network.SocialNetwork.repository.PostRepository;
import com.network.SocialNetwork.repository.StatisticRepository;
import com.network.SocialNetwork.repository.UserRepository;
import com.network.SocialNetwork.service.PostService;
import com.network.SocialNetwork.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private StatisticRepository statisticRepository;

    public String GetUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping
    public String AdminIndex() {
        return "admin/adminIndex";
    }

    @GetMapping("/user-list")
    public String user_Management(Model model) {
        // User List
        List<User> roleUserList = userRepository.findUsersByRoleId(2L); // Kiểu Long nên phải để L ở đằng sau
        model.addAttribute("users", roleUserList);
        return "admin/listUser";
    }

    @GetMapping("/user-list/detail-user/{id}")
    public String DetailUser(@PathVariable Long id, Model model) {
        // Navigation Bar
        String username = GetUserName();
        Optional<User> currentlyUser = userRepository.findByUsername(username);
        currentlyUser.ifPresent(value -> model.addAttribute("user", value));

        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            User userChosen = userOptional.get();
            model.addAttribute("userChosen", userChosen);
            return "admin/detail-user";
        }
        return "redirect:/error";
    }

    @PostMapping("/user-list/detail-user/{id}")
    public String updateAccountStatus(@PathVariable Long id, @RequestParam("accountStatus") boolean accountStatus) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            User userChosen = userOptional.get();
            userChosen.setAccountStatus(accountStatus);
            userService.saveButNotHash(userChosen);
        }
        return "redirect:/admin/user-list/detail-user/{id}";
    }

    @GetMapping("/statistic")
    public String statistic(Model model) {
        var listUser = userRepository.findAll();
        listUser.removeIf(m -> m.getRole().getName().equals("ADMIN"));
        model.addAttribute("listUser", listUser);

        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(6); // Để lấy 6 ngày trước cộng với ngày hiện tại

        List<Statistic> statistics = statisticRepository.findAllByVisitAtBetween(sevenDaysAgo, today.plusDays(1));

        List<Long> adminUserIds = userRepository.findAll().stream()
                .filter(user -> user.getRole().getName().equals("ADMIN"))
                .map(User::getId)
                .collect(Collectors.toList());

        Map<LocalDate, Long> visitsPerDay = statistics.stream()
                .filter(stat -> !adminUserIds.contains(stat.getVisitors()))
                .collect(Collectors.groupingBy(Statistic::getVisitAt, Collectors.counting()));

        for (int i = 1; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            visitsPerDay.putIfAbsent(date, 0L);
        }

        List<LocalDate> sortedDates = visitsPerDay.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        List<Long> sortedCounts = sortedDates.stream()
                .map(visitsPerDay::get)
                .collect(Collectors.toList());

        model.addAttribute("sortedDates", sortedDates);
        model.addAttribute("sortedCounts", sortedCounts);

        // Lấy thống kê số bài viết trong 7 ngày qua
    Map<LocalDate, Long> postCounts = postService.getPostCountsLast7Days();

    List<LocalDate> postDates = postCounts.keySet().stream()
            .sorted()
            .collect(Collectors.toList());

    List<Long> postCountsSorted = postDates.stream()
            .map(postCounts::get)
            .collect(Collectors.toList());

    model.addAttribute("postDates", postDates);
    model.addAttribute("postCounts", postCountsSorted);

    return "admin/statistic";
        
    }
}
