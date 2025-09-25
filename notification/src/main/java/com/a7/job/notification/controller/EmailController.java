package com.a7.job.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notification/email")
public class EmailController {
    @GetMapping
    public ResponseEntity<String> sendNotification(
            @RequestHeader("X-User-Email") String email,
            @RequestHeader(value = "Authorization", required = false) String token) {

        System.out.println("Email header: " + email);
        System.out.println("Auth token header: " + token);

        return ResponseEntity.ok("Notification sent to " + email);
    }
}
