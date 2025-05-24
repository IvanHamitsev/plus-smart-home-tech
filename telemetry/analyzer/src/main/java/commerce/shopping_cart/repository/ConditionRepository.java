package commerce.shopping_cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import commerce.shopping_cart.model.Condition;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
}
