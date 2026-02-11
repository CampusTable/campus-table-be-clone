package shop.campustable.campustablebeclone.domain.cart.repository;

import io.swagger.v3.oas.annotations.Parameter;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.campustable.campustablebeclone.domain.cart.entity.Cart;
import shop.campustable.campustablebeclone.domain.user.entity.User;

public interface CartRepository extends JpaRepository<Cart, Long> {

  @Query("SELECT DISTINCT c FROM Cart c " +
      "LEFT JOIN FETCH c.cartItems ci " +
      "LEFT JOIN FETCH ci.menu " +
      "WHERE c.user = :user")
  Optional<Cart> findByUserWithItems(@Param("user") User user);

  Optional<Cart> findByUser(User user);
}
