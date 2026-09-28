package com.seatlock.booking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsBySeatIdAndStatus(Long seatId, String status);


}