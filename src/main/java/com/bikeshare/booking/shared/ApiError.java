package com.bikeshare.booking.shared;

import java.time.OffsetDateTime;

public class ApiError {
    public String error;
    public String message;
    public int status;
    public OffsetDateTime timestamp = OffsetDateTime.now();

    public ApiError(String error, String message, int status) {
        this.error = error;
        this.message = message;
        this.status = status;
    }
}