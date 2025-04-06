package commerce.shopping_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import commerce.shopping_store.model.Action;

public interface ActionRepository extends JpaRepository<Action, Long> {
}
