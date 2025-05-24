package commerce.shopping_cart.repository;

import commerce.shopping_cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<Cart, String> {
    public Optional<Cart> findByUsername(String username);
}
