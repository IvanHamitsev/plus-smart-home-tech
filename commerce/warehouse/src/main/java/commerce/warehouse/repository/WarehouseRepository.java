package commerce.warehouse.repository;

import commerce.warehouse.model.ProductInWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<ProductInWarehouse, UUID> {
}
