package com.seatlock.booking;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.seatlock.booking.dto.CreateReservationRequest;
import com.seatlock.booking.dto.ReservationResponse;
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final BookingService bookingService;

    public ReservationController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse reserveSeat(@RequestBody CreateReservationRequest request, Authentication authentication) {
        return bookingService.reserveSeat(authentication.getName(),request);
    }
}