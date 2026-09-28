package com.seatlock.event.dto;

import java.math.BigDecimal;

public record SeatResponse(
        Long id,
        String seatNumber,
        BigDecimal price,
        Long eventId
) {
}