package commerce.warehouse.repository;

import commerce.warehouse.model.ProductInWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<ProductInWarehouse, String> {
    Integer getQuantityById(String id);
}
