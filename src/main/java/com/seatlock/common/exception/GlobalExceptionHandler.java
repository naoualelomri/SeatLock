package com.seatlock.common.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(SeatNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSeatNotFound( SeatNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(SeatAlreadyBookedException.class)
    public ResponseEntity<Map<String, String>> handleSeatAlreadyBooked(
            SeatAlreadyBookedException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", ex.getMessage()));
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials(InvalidCredentialsException ex) {
      return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("message", ex.getMessage()));
    }  

    @ExceptionHandler(SeatAlreadyHeldException.class)
    public ResponseEntity<Map<String, String>> handleSeatAlreadyHeld(SeatAlreadyHeldException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(Map.of("message", ex.getMessage()));
   }

   @ExceptionHandler(SeatHoldRequiredException.class)
   public ResponseEntity<Map<String, String>> handleSeatHoldRequired(SeatHoldRequiredException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(Map.of("message", ex.getMessage()));
        }
}