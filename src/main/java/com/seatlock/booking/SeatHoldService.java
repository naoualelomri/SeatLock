package com.seatlock.booking;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.seatlock.common.exception.SeatAlreadyHeldException;
import com.seatlock.common.exception.SeatNotFoundException;
import com.seatlock.event.SeatRepository;

@Service 
public class SeatHoldService {
    private final StringRedisTemplate redisTemplate;
    private final SeatRepository seatRepository;

    public SeatHoldService(StringRedisTemplate redisTemplate,SeatRepository seatRepository) {
        this.redisTemplate = redisTemplate;
        this.seatRepository = seatRepository;
    }

    public void holdSeat(Long seatId, String userEmail){
        if (!seatRepository.existsById(seatId)) {
            throw new SeatNotFoundException(seatId);
        }

        String key = "seat-hold:" + seatId;
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(key, userEmail, Duration.ofMinutes(2)) ;

        if (!Boolean.TRUE.equals(acquired)) {
            throw new SeatAlreadyHeldException(seatId);
        }
    }

    public boolean isHeldByUser(Long seatId, String userEmail) {

        String key = "seat-hold:" + seatId;

        String holder = redisTemplate
            .opsForValue()
            .get(key);

        return userEmail.equals(holder);
    }
    public void releaseHold(Long seatId) {

        String key = "seat-hold:" + seatId;

        redisTemplate.delete(key);
}
}
