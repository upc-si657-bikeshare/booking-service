package com.bikeshare.booking.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AppHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        return Health.up()
                .withDetail("app", "bikeshare-api")
                .withDetail("timestamp", Instant.now().toString())
                .build();
    }
}