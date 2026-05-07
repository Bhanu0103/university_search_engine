package com.university.notification.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {
    private final Map<String, List<String>> notificationsByUser = new ConcurrentHashMap<>();

    public void sendAlert(String message) {
        System.out.println("==========================================");
        System.out.println("NOTIFICATION ALERT: " + message);
        System.out.println("==========================================");
    }

    public String logSearchEvent(String userId, String query) {
        String message = "Search event logged for user " + userId + ": " + query;
        sendAlert(message);
        notificationsByUser.computeIfAbsent(userId, key -> new ArrayList<>()).add(message);
        return message;
    }

    public String sendSearchAlert(String userId, String alertMessage) {
        String message = "Search alert for user " + userId + ": " + alertMessage;
        sendAlert(message);
        notificationsByUser.computeIfAbsent(userId, key -> new ArrayList<>()).add(message);
        return message;
    }

    public String notifyNewContent(String documentId) {
        String message = "New content available for document: " + documentId;
        sendAlert(message);
        notificationsByUser.computeIfAbsent("broadcast", key -> new ArrayList<>()).add(message);
        return message;
    }

    public List<String> getNotifications(String userId) {
        List<String> notifications = new ArrayList<>();
        notifications.addAll(notificationsByUser.getOrDefault(userId, List.of()));
        notifications.addAll(notificationsByUser.getOrDefault("broadcast", List.of()));
        return notifications;
    }
}
