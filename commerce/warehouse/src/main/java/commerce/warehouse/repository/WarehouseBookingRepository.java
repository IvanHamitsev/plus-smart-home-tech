package commerce.warehouse.repository;

import commerce.warehouse.model.OrderBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WarehouseBookingRepository extends JpaRepository<OrderBooking, UUID> {
}
