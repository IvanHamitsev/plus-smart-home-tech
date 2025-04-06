package commerce.shopping_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import commerce.shopping_store.model.Condition;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
}
