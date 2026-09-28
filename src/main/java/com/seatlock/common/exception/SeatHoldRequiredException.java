package com.seatlock.common.exception;

public class SeatHoldRequiredException extends RuntimeException {

    public SeatHoldRequiredException(Long seatId) {
        super("You must hold seat " + seatId + " before booking it");
    }
}