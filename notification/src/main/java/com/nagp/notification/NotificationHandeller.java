package com.nagp.notification;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationHandeller {

    @PostMapping("/api/notification/send")
    public String handleNotification(@RequestBody String notification) {
        System.out.println("Received notification : " + notification);
        return "Notification received " + notification;
    }

}
