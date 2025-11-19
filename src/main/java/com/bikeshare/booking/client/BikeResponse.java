package com.bikeshare.booking.client;

import java.math.BigDecimal;

public record BikeResponse(
        Long id,
        BigDecimal costPerMinute,
        String status,
        Long ownerId
) {}