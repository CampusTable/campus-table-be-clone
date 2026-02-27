package shop.campustable.campustablebeclone.domain.cart.repository;

import static shop.campustable.campustablebeclone.domain.cafeteria.entity.QCafeteria.cafeteria;
import static shop.campustable.campustablebeclone.domain.cart.entity.QCart.cart;
import static shop.campustable.campustablebeclone.domain.cart.entity.QCartItem.cartItem;
import static shop.campustable.campustablebeclone.domain.category.entity.QCategory.category;
import static shop.campustable.campustablebeclone.domain.menu.entity.QMenu.menu;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import shop.campustable.campustablebeclone.domain.cart.entity.Cart;
import shop.campustable.campustablebeclone.domain.user.entity.User;

@RequiredArgsConstructor
public class CartRepositoryCustomImpl implements CartRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<Cart> findByUserWithItems(User user) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(cart)
            .distinct()
            .leftJoin(cart.cartItems, cartItem).fetchJoin()
            .leftJoin(cartItem.menu, menu).fetchJoin()
            .leftJoin(menu.category, category).fetchJoin()
            .where(cart.user.id.eq(user.getId()))
            .fetchOne()
    );
  }

  @Override
  public Optional<Cart> findCartForCheckout(User user) {
    return Optional.ofNullable(
        queryFactory
            .selectFrom(cart)
            .distinct()
            .join(cart.user).fetchJoin()
            .leftJoin(cart.cartItems, cartItem).fetchJoin()
            .leftJoin(cartItem.menu, menu).fetchJoin()
            .leftJoin(menu.category, category).fetchJoin()
            .leftJoin(category.cafeteria, cafeteria).fetchJoin()
            .where(cart.user.eq(user))
            .fetchOne()
    );
  }


}
