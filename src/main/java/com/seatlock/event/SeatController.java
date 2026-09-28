package com.seatlock.event;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.seatlock.event.dto.CreateSeatRequest;
import com.seatlock.event.dto.SeatResponse;

@RestController
@RequestMapping("/api/events/{eventId}/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Seat addSeatToEvent(
            @PathVariable Long eventId,
            @RequestBody CreateSeatRequest request
    ) {
        return seatService.addSeatToEvent(eventId, request);
    }

    @GetMapping
    public List<SeatResponse> getSeatsByEvent(@PathVariable Long eventId) {
         return seatService.getSeatsByEvent(eventId);
    }
}