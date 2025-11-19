package com.bikeshare.booking.reservation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Gestión de reservas de bicicletas")
public class ReservationController {

    private final ReservationService service;

    @Operation(summary = "Crear una nueva reserva")
    @PostMapping
    public ResponseEntity<ReservationDTO> create(@Valid @RequestBody CreateReservationDTO dto) {
        Reservation saved = service.create(dto);
        URI location = URI.create("/api/reservations/" + saved.getId());
        return ResponseEntity.created(location).body(ReservationDTO.from(saved));
    }

    @Operation(summary = "Obtener una reserva por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> get(@PathVariable Long id) {
        return service.get(id)
                .map(r -> ResponseEntity.ok(ReservationDTO.from(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar reservas", description = "Permite filtrar por renterId, bikeId y status")
    @GetMapping
    public List<ReservationDTO> list(
            @Parameter(description = "ID del arrendatario (renter)") @RequestParam(required = false) Long renterId,
            @Parameter(description = "ID de la bicicleta") @RequestParam(required = false) Long bikeId, // CAMBIO: ownerId -> bikeId
            @Parameter(description = "Estado de la reserva") @RequestParam(required = false) ReservationStatus status
    ) {
        return service.list(renterId, bikeId, status).stream()
                .map(ReservationDTO::from)
                .toList();
    }

    @Operation(summary = "Actualizar el estado de una reserva")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ReservationDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusDTO dto
    ) {
        Reservation updated = service.updateStatus(id, dto.newStatus());
        return ResponseEntity.ok(ReservationDTO.from(updated));
    }
}