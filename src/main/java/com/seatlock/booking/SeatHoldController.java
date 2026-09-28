package com.seatlock.booking;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seats")
public class SeatHoldController {

    private final SeatHoldService seatHoldService;

    public SeatHoldController(SeatHoldService seatHoldService) {
        this.seatHoldService = seatHoldService;
    }

    @PostMapping("/{seatId}/hold")
    public ResponseEntity<Map<String, String>> holdSeat(@PathVariable Long seatId,Authentication authentication) {
        seatHoldService.holdSeat(
                seatId,
                authentication.getName()
        );

        return ResponseEntity.ok(
                Map.of("message", "Seat held successfully")
        );
    }
}