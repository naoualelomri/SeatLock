package com.seatlock.event;

public record BookingConfirmedEvent(Long reservationId,Long seatId,String userEmail) {

}
