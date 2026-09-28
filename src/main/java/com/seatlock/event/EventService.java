package com.seatlock.event;
import java.util.List;

import org.springframework.stereotype.Service;

import com.seatlock.event.dto.CreateEventRequest;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(CreateEventRequest request) {

        Event event = new Event();

        event.setName(request.name());
        event.setLocation(request.location());
        event.setEventDate(request.eventDate());

        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
         return eventRepository.findAll();
    }
}