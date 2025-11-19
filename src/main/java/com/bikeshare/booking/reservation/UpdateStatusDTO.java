package com.bikeshare.booking.reservation;

import jakarta.validation.constraints.NotNull;

public record UpdateStatusDTO(
        @NotNull ReservationStatus newStatus
) {}