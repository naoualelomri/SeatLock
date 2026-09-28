package com.seatlock.common.exception;

public class SeatAlreadyHeldException extends RuntimeException {

    public SeatAlreadyHeldException(Long seatId) {
        super("Seat is currently held by another user: " + seatId);
    }
}