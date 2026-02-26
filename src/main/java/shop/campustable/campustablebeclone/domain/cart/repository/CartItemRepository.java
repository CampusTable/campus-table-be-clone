package shop.campustable.campustablebeclone.domain.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.campustable.campustablebeclone.domain.cart.entity.Cart;
import shop.campustable.campustablebeclone.domain.cart.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("delete from CartItem ci where ci.cart = :cart")
  void deleteAllByCart(@Param("cart") Cart cart);
}
