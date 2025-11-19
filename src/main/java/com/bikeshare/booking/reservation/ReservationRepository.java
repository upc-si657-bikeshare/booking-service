package com.bikeshare.booking.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByBikeIdAndStatusIn(Long bikeId, List<ReservationStatus> statuses);

    List<Reservation> findByRenterId(Long renterId);
    List<Reservation> findByBikeId(Long bikeId);
    List<Reservation> findByStatus(ReservationStatus status);
}