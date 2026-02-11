package shop.campustable.campustablebeclone.domain.cart.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.campustable.campustablebeclone.domain.menu.entity.Menu;
import shop.campustable.campustablebeclone.domain.user.entity.User;
import shop.campustable.campustablebeclone.global.exception.CustomException;
import shop.campustable.campustablebeclone.global.exception.ErrorCode;

@Entity
@Getter
@NoArgsConstructor
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "cafeteria",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private List<CartItem> cartItems = new ArrayList<>();

  private Long cafeteriaId;

  public Cart(User user) {
    this.user = user;
  }

  public void addOrUpdateItem(Menu menu, int quantity) {
    Long menuCafeteriaId = menu.getCategory().getCafeteria().getId();
    if (this.cafeteriaId != null && this.cafeteriaId.equals(menuCafeteriaId) && !cartItems.isEmpty()) {
      throw new CustomException(ErrorCode.CART_MIXED_CAFETERIA);
    }
    this.cafeteriaId = menuCafeteriaId;

    if(quantity > 9)
      throw new CustomException(ErrorCode.CART_ITEM_QUANTITY_LIMIT);

    this.cartItems.stream()
        .filter(cartItem -> cartItem.getMenu().getId().equals(menu.getId()))
        .findFirst()
        .ifPresentOrElse(
            // cart에 있는 메뉴라면
            cartItem -> cartItem.updateQuantity(quantity),
            // 없다면
            () -> this.cartItems.add(CartItem.builder()
                .cart(this)
                .menu(menu)
                .quantity(quantity)
                .build())
        );
  }

  public void clearCart(){
    this.cartItems.clear();
    this.cafeteriaId = null;
  }


}
