package com.seatlock.notification;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.seatlock.event.BookingConfirmedEvent;

@Component
public class NotificationConsumer {

    private final StringRedisTemplate redisTemplate;

    public NotificationConsumer(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @KafkaListener(
            topics = "booking-confirmed",
            groupId = "notification-service"
    )
    public void handleBookingConfirmed(BookingConfirmedEvent event) {

        String key =
                "processed:booking-confirmed:"
                + event.reservationId();

        Boolean firstProcessing =
                redisTemplate.opsForValue()
                        .setIfAbsent(
                                key,
                                "1",
                                Duration.ofHours(24)
                        );

        if (!Boolean.TRUE.equals(firstProcessing)) {

            System.out.println(
                    "Duplicate booking event ignored: "
                    + event.reservationId()
            );

            return;
        }

        try {

            System.out.println(
                    "Notification: booking "
                    + event.reservationId()
                    + " confirmed for "
                    + event.userEmail()
                    + " - seat "
                    + event.seatId()
            );

        } catch (RuntimeException e) {

            redisTemplate.delete(key);

            throw e;
        }
    }
}