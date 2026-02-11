package shop.campustable.campustablebeclone.domain.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.campustable.campustablebeclone.domain.cart.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
