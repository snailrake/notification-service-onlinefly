package ru.onlinefly.notification_service.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.onlinefly.notification_service.dto.event.FlyEvent;
import ru.onlinefly.notification_service.service.mail.MailService;

@Slf4j
@Component
@RequiredArgsConstructor
public class FlyEventListener {

    private final MailService mailService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.data.kafka.channels.fly-channel.name}", groupId = "${spring.data.kafka.group-id}")
    public void listen(String event) {
        try {
            FlyEvent flyEvent = objectMapper.readValue(event, FlyEvent.class);
            log.info("FlyEvent received: {}", flyEvent);
            mailService.sendMail(flyEvent.getEmail(), "Результаты тестирования (Онлайн-Летучка)", getMessage(flyEvent));
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }

    private String getMessage(FlyEvent flyEvent) {
        long avgTime = flyEvent.getTime();
        long minutes = avgTime / 60;
        long seconds = avgTime % 60;
        String formattedTime = String.format("%02d:%02d", minutes, seconds);
        return String.format("""
                Студент: %s
                Тест: %s
                Результат: %.1f%%
                Время выполнения теста: %s
                """, flyEvent.getName(), flyEvent.getFlyName(), flyEvent.getScore(), formattedTime);
    }
}