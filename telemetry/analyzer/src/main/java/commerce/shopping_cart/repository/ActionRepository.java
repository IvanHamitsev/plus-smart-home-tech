package commerce.shopping_cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import commerce.shopping_cart.model.Action;

public interface ActionRepository extends JpaRepository<Action, Long> {
}
