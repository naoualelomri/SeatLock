package com.seatlock.user.dto;

public record LoginResponse(Long id,String email,String role, String token) {

}
