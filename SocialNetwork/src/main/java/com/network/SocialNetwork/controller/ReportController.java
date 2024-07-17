package com.network.SocialNetwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.network.SocialNetwork.service.NotificationService;
import com.network.SocialNetwork.service.ReportService;


@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private NotificationService notificationService;
    
    @PostMapping("/send-report")
    public String sendReport(@RequestParam("postId") Long postId,
                             @RequestParam("reason") String reason,
                             @RequestParam("userIdReport") Long userIdReport,
                             @Param("description") String description,
                             RedirectAttributes redirectAttributes)
    {
        reportService.createReport(postId, reason, userIdReport, description);
        notificationService.sendReportToAdmin(postId, reason, userIdReport);

        redirectAttributes.addFlashAttribute("message","Gửi report thành công");
        return "redirect:/";
    }
}
