package com.bikeshare.booking.reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationDTO(
        Long id,
        Long renterId,
        Long bikeId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal totalPrice,
        ReservationStatus status
) {
    public static ReservationDTO from(Reservation r) {
        return new ReservationDTO(
                r.getId(),
                r.getRenterId(),
                r.getBikeId(),
                r.getStartDate(),
                r.getEndDate(),
                r.getTotalPrice(),
                r.getStatus()
        );
    }
}