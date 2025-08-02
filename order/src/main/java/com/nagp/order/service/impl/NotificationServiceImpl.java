package com.nagp.order.service.impl;

import com.nagp.order.service.NotificationService;
import com.nagp.order.utils.NotificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationUtils notificationUtils;
    @Override
    public void sendNotification(String message) {
        notificationUtils.sendNotification("Notification sent from Order Service : " + message);
    }
}
