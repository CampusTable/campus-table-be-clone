package shop.campustable.campustablebeclone.domain.cart.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.campustable.campustablebeclone.domain.cart.entity.Cart;
import shop.campustable.campustablebeclone.domain.user.entity.User;

public interface CartRepository extends JpaRepository<Cart, Long> {

  Optional<Cart> findByUser(User user);
}
