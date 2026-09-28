package com.seatlock.event;
import java.util.List ;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {  // premier type generique est lentité manipulé et le deuxieme est le type de lidentifiant
    List<Seat> findByEventId(Long eventId);

}
