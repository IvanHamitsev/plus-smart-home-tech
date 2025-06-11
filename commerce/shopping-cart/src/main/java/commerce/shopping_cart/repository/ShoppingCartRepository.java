package commerce.shopping_cart.repository;

import commerce.shopping_cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartRepository extends JpaRepository<Cart, UUID> {
    public Optional<Cart> findByOwner(String username);
}
