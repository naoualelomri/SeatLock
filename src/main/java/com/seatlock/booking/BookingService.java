package com.seatlock.booking;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seatlock.booking.dto.CreateReservationRequest;
import com.seatlock.booking.dto.ReservationResponse;
import com.seatlock.common.exception.SeatAlreadyBookedException;
import com.seatlock.common.exception.SeatHoldRequiredException;
import com.seatlock.common.exception.SeatNotFoundException;
import com.seatlock.event.BookingConfirmedEvent;
import com.seatlock.event.BookingEventProducer;
import com.seatlock.event.Seat;
import com.seatlock.event.SeatRepository;
import com.seatlock.user.User;
import com.seatlock.user.UserRepository;

@Service 
public class BookingService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final SeatHoldService seatHoldService;
    private final BookingEventProducer bookingEventProducer;

    public BookingService(ReservationRepository reservationRepository,UserRepository userRepository, SeatRepository seatRepository, SeatHoldService seatHoldService , BookingEventProducer bookingEventProducer) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.seatRepository = seatRepository;
        this.seatHoldService = seatHoldService;
        this.bookingEventProducer= bookingEventProducer ;
    }

    @Transactional
    public ReservationResponse reserveSeat(String userEmail, CreateReservationRequest request) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
        Seat seat = seatRepository.findById(request.seatId()).orElseThrow(() -> new SeatNotFoundException(request.seatId()));
        if (reservationRepository.existsBySeatIdAndStatus(seat.getId(), "CONFIRMED")){
            throw new SeatAlreadyBookedException(seat.getId());
        }

        if (!seatHoldService.isHeldByUser(seat.getId(),userEmail)){
            throw new SeatHoldRequiredException(seat.getId());
        }


        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setSeat(seat);
        reservation.setStatus("CONFIRMED");
        reservation.setCreatedAt(LocalDateTime.now());

        Reservation savedReservation = reservationRepository.save(reservation);

        seatHoldService.releaseHold(seat.getId());

        BookingConfirmedEvent event = new BookingConfirmedEvent(savedReservation.getId(), seat.getId(), user.getEmail()) ;
        bookingEventProducer.publishBookingConfirmed(event);
        
        return new ReservationResponse(
                savedReservation.getId(),
                savedReservation.getUser().getId(),
                savedReservation.getSeat().getId(),
                savedReservation.getStatus(),
                savedReservation.getCreatedAt()
        );
    }

}
