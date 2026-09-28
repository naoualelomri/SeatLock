package com.seatlock.event.dto;

import java.time.LocalDateTime ; 

public record CreateEventRequest(String name , String location , LocalDateTime eventDate) {

}
