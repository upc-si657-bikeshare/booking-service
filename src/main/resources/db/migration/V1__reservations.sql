-- RESERVATIONS
CREATE TABLE reservations (
                              id           BIGSERIAL PRIMARY KEY,
                              renter_id    BIGINT NOT NULL,
                              bike_id      BIGINT NOT NULL,
                              start_date   TIMESTAMP NOT NULL,
                              end_date     TIMESTAMP NOT NULL,
                              total_price  NUMERIC(12,2),
                              status       VARCHAR(24) NOT NULL,
                              CONSTRAINT chk_reservation_window CHECK (end_date > start_date)
);

CREATE INDEX idx_reservations_bike ON reservations(bike_id, start_date);
CREATE INDEX idx_reservations_renter ON reservations(renter_id, start_date);