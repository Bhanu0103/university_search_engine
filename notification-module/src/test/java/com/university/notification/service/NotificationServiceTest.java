package com.university.notification.service;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class NotificationServiceTest {
    private final NotificationService service = new NotificationService();

    @Test
    void notifyNewContentAddsBroadcastNotification() {
        String message = service.notifyNewContent("6");

        assertThat(message).isEqualTo("New content available for document: 6");
        assertThat(service.getNotifications("any-user")).contains(message);
    }

    @Test
    void userNotificationsIncludeDirectAndBroadcastMessages() {
        service.sendSearchAlert("1", "Check engineering updates");
        service.notifyNewContent("9");

        assertThat(service.getNotifications("1"))
                .contains("Search alert for user 1: Check engineering updates")
                .contains("New content available for document: 9");
    }
}
