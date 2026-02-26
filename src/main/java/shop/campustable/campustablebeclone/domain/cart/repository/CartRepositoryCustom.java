package shop.campustable.campustablebeclone.domain.cart.repository;

import java.util.Optional;
import shop.campustable.campustablebeclone.domain.cart.entity.Cart;
import shop.campustable.campustablebeclone.domain.user.entity.User;

public interface CartRepositoryCustom {

  Optional<Cart> findByUserWithItems(User user);

}
