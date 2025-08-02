package com.nagp.order.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class NotificationUtils {

    @Autowired
    private RestTemplate restTemplate;

    public void sendNotification(String notification) {
        String url = "http://NOTIFICATION-SERVICE/api/notification/send";

        try {
            restTemplate.postForObject(url, notification, String.class);
        } catch (Exception e) {
            log.error("Error sending notification: {}", e.getMessage());
       }
    }
}
