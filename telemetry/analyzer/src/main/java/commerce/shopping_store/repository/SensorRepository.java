package commerce.shopping_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import commerce.shopping_store.model.Sensor;

import java.util.Collection;
import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, String> {
    boolean existsByIdInAndHubId(Collection<String> ids, String hubId);

    Optional<Sensor> findByIdAndHubId(String id, String hubId);

}
