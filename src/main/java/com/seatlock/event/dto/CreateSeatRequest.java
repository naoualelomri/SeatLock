package com.seatlock.event.dto;

import java.math.BigDecimal;

public record CreateSeatRequest(String seatNumber,BigDecimal price) {
}