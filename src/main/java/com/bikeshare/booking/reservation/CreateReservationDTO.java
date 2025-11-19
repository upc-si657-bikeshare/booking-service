package com.bikeshare.booking.reservation;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateReservationDTO(
        @NotNull Long renterId,
        @NotNull Long bikeId,
        @NotNull LocalDateTime startDate,
        @NotNull LocalDateTime endDate
) {}