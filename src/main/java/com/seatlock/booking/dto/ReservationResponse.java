package com.seatlock.booking.dto;

import java.time.LocalDateTime;

public record ReservationResponse(Long id, Long userId, Long seatId, String status, LocalDateTime createdAt) {

}
