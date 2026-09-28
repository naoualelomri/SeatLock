package com.seatlock.event;
import org.springframework.stereotype.Service;

import com.seatlock.event.dto.CreateSeatRequest;
import com.seatlock.event.dto.SeatResponse;

import java.util.List;
@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final EventRepository eventRepository;

    public SeatService(
            SeatRepository seatRepository,
            EventRepository eventRepository
    ) {
        this.seatRepository = seatRepository;
        this.eventRepository = eventRepository;
    }

    public Seat addSeatToEvent(Long eventId, CreateSeatRequest request){
        Event event = eventRepository.findById(eventId).orElseThrow(()-> new IllegalArgumentException("Event not found"));
        Seat seat = new Seat();
        seat.setSeatNumber(request.seatNumber());
        seat.setPrice(request.price());
        seat.setEvent(event);
        return seatRepository.save(seat);
    }
    public List<SeatResponse> getSeatsByEvent(Long eventId){
        return seatRepository.findByEventId(eventId)
                .stream()
                .map(seat-> new SeatResponse(
                    seat.getId(),
                    seat.getSeatNumber(),
                    seat.getPrice(),
                    seat.getEvent().getId()
                
            ))
            .toList();
    }
}