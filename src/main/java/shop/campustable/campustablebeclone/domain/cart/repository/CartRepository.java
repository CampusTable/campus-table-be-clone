package shop.campustable.campustablebeclone.domain.cart.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.campustable.campustablebeclone.domain.cart.entity.Cart;
import shop.campustable.campustablebeclone.domain.user.entity.User;

public interface CartRepository extends JpaRepository<Cart, Long>, CartRepositoryCustom {

  @Query("select distinct c from Cart c " +
         "join fetch c.user " +
         "left join fetch c.cartItems ci " +
         "left join fetch ci.menu m " +
         "left join fetch m.category cat " +
         "left join fetch cat.cafeteria caf " +
         "where c.user = :user")
  Optional<Cart> findCartForCheckout(@Param("user") User user);

  Optional<Cart> findByUser(User user);
}
