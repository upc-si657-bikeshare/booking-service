package com.bikeshare.booking.reservation;

import com.bikeshare.booking.client.BikeClient;
import com.bikeshare.booking.client.BikeResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepo;
    private final BikeClient bikeClient; // <--- Usamos el Cliente Feign

    /**
     * Crea una nueva reserva.
     * @param dto Datos para la creación de la reserva.
     * @return La reserva guardada.
     */
    @Transactional
    public Reservation create(CreateReservationDTO dto) {
        // 1. Validar y obtener datos de la bici desde el microservicio Catalog (HTTP)
        // Si la bici no existe o el servicio está caído, esto lanzará una excepción (FeignException)
        BikeResponse bike = bikeClient.getBike(dto.bikeId());

        // 2. Validaciones de fechas
        if (dto.startDate().isAfter(dto.endDate())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        // 3. Validar disponibilidad local (si ya hay reservas aceptadas para esa bici en estas fechas)
        // Nota: Esto valida contra nuestra propia base de datos de reservas.
        boolean isBusy = reservationRepo.existsByBikeIdAndStatusIn(
                dto.bikeId(),
                List.of(ReservationStatus.PENDING, ReservationStatus.ACCEPTED)
        );

        if (isBusy) {
            throw new IllegalStateException("La bicicleta ya se encuentra reservada o con una reserva pendiente.");
        }

        // 4. Calcular precio
        long minutes = Duration.between(dto.startDate(), dto.endDate()).toMinutes();
        BigDecimal price = bike.costPerMinute().multiply(BigDecimal.valueOf(minutes));

        // 5. Crear la entidad usando IDs
        Reservation r = Reservation.builder()
                .renterId(dto.renterId())
                .bikeId(dto.bikeId())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .totalPrice(price)
                .status(ReservationStatus.PENDING)
                .build();

        return reservationRepo.save(r);
    }

    /**
     * Obtiene una reserva por su ID.
     */
    public Optional<Reservation> get(Long id) {
        return reservationRepo.findById(id);
    }

    /**
     * Lista las reservas con filtros opcionales.
     * Nota: Filtramos por bikeId en lugar de ownerId, ya que no tenemos el ownerId almacenado directamente
     * en la tabla de reservas ni acceso a la DB de usuarios.
     */
    @Transactional(readOnly = true)
    public List<Reservation> list(Long renterId, Long bikeId, ReservationStatus status) {
        if (renterId != null) return reservationRepo.findByRenterId(renterId);
        if (bikeId != null) return reservationRepo.findByBikeId(bikeId);
        if (status != null) return reservationRepo.findByStatus(status);
        return reservationRepo.findAll();
    }

    /**
     * Actualiza el estado de una reserva.
     */
    @Transactional
    public Reservation updateStatus(Long reservationId, ReservationStatus newStatus) {
        Reservation reservation = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found with id: " + reservationId));

        reservation.setStatus(newStatus);
        return reservationRepo.save(reservation);
    }
}