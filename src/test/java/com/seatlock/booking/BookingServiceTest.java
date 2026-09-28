package com.seatlock.booking;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seatlock.booking.dto.CreateReservationRequest;
import com.seatlock.booking.dto.ReservationResponse;
import com.seatlock.common.exception.SeatAlreadyBookedException;
import com.seatlock.common.exception.SeatHoldRequiredException;
import com.seatlock.event.BookingConfirmedEvent;
import com.seatlock.event.BookingEventProducer;
import com.seatlock.event.Seat;
import com.seatlock.event.SeatRepository;
import com.seatlock.user.User;
import com.seatlock.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private SeatHoldService seatHoldService;

    @Mock
    private BookingEventProducer bookingEventProducer;

    @InjectMocks
    private BookingService bookingService;


    @Test
    void shouldReserveSeatSuccessfully() {

        String email = "alice@example.com";
        Long seatId = 5L;

        User user = new User();
        user.setId(2L);
        user.setEmail(email);

        Seat seat = new Seat();
        seat.setId(seatId);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(seatRepository.findById(seatId))
                .thenReturn(Optional.of(seat));

        when(reservationRepository
                .existsBySeatIdAndStatus(seatId, "CONFIRMED"))
                .thenReturn(false);

        when(seatHoldService.isHeldByUser(seatId, email))
                .thenReturn(true);

        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> {

                    Reservation reservation =
                            invocation.getArgument(0);

                    reservation.setId(10L);

                    return reservation;
                });

        CreateReservationRequest request =
                new CreateReservationRequest(seatId);

        ReservationResponse response =
                bookingService.reserveSeat(email, request);

        assertEquals(10L, response.id());
        assertEquals(2L, response.userId());
        assertEquals(5L, response.seatId());
        assertEquals("CONFIRMED", response.status());

        verify(reservationRepository)
                .save(any(Reservation.class));

        verify(seatHoldService)
                .releaseHold(seatId);

        verify(bookingEventProducer)
                .publishBookingConfirmed(
                        any(BookingConfirmedEvent.class)
                );
    }


    @Test
    void shouldRejectReservationWithoutHold() {

        String email = "alice@example.com";
        Long seatId = 5L;

        User user = new User();
        user.setId(2L);
        user.setEmail(email);

        Seat seat = new Seat();
        seat.setId(seatId);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(seatRepository.findById(seatId))
                .thenReturn(Optional.of(seat));

        when(reservationRepository
                .existsBySeatIdAndStatus(seatId, "CONFIRMED"))
                .thenReturn(false);

        when(seatHoldService.isHeldByUser(seatId, email))
                .thenReturn(false);

        CreateReservationRequest request =
                new CreateReservationRequest(seatId);

        assertThrows(
                SeatHoldRequiredException.class,
                () -> bookingService.reserveSeat(email, request)
        );

        verify(reservationRepository, never())
                .save(any());

        verify(bookingEventProducer, never())
                .publishBookingConfirmed(any());
    }


    @Test
    void shouldRejectAlreadyBookedSeat() {

        String email = "alice@example.com";
        Long seatId = 5L;

        User user = new User();
        user.setId(2L);
        user.setEmail(email);

        Seat seat = new Seat();
        seat.setId(seatId);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(seatRepository.findById(seatId))
                .thenReturn(Optional.of(seat));

        when(reservationRepository
                .existsBySeatIdAndStatus(seatId, "CONFIRMED"))
                .thenReturn(true);

        CreateReservationRequest request =
                new CreateReservationRequest(seatId);

        assertThrows(
                SeatAlreadyBookedException.class,
                () -> bookingService.reserveSeat(email, request)
        );

        verify(reservationRepository, never())
                .save(any());

        verify(seatHoldService, never())
                .releaseHold(anyLong());

        verify(bookingEventProducer, never())
                .publishBookingConfirmed(any());
    }
}