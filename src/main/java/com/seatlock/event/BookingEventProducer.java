package com.seatlock.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service

public class BookingEventProducer {

    private static final String TOPIC = "booking-confirmed";

    private final KafkaTemplate<String, BookingConfirmedEvent> kafkaTemplate;

    public BookingEventProducer(KafkaTemplate<String, BookingConfirmedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishBookingConfirmed(BookingConfirmedEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}