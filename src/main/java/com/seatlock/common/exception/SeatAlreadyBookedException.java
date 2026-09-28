package com.seatlock.common.exception;

public class SeatAlreadyBookedException extends RuntimeException {

    public SeatAlreadyBookedException(Long seatId) {
        super("Seat is already booked: " + seatId);
    }
}