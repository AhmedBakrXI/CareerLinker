package com.a7.job.notification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notification/email")
public class EmailController {
    @GetMapping
    public String sendEmail() {
        return "Email sent successfully!";
    }
}
