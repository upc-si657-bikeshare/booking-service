package com.bikeshare.booking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.math.BigDecimal;

@FeignClient(name = "catalog-service", url = "http://localhost:8082")
public interface BikeClient {

    @GetMapping("/api/bikes/{id}")
    BikeResponse getBike(@PathVariable("id") Long id);
}