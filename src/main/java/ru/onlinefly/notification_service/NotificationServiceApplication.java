package ru.onlinefly.notification_service;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class NotificationServiceApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(NotificationServiceApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }
}
