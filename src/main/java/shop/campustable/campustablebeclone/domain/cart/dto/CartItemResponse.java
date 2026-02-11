package shop.campustable.campustablebeclone.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.campustable.campustablebeclone.domain.cart.entity.CartItem;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {

  private Long cartItemId;
  private Long menuId;
  private String menuName;
  private String menuUrl;
  private int price;
  private int quantity;
  private int subtotal;

  public static CartItemResponse from(CartItem cartItem, String fullUrl) {

    int menuPrice = cartItem.getMenu().getPrice();
    int menuQuantity = cartItem.getQuantity();

    return CartItemResponse.builder()
        .cartItemId(cartItem.getId())
        .menuId(cartItem.getMenu().getId())
        .menuName(cartItem.getMenu().getMenuName())
        .menuUrl(fullUrl)
        .price(cartItem.getMenu().getPrice())
        .quantity(cartItem.getQuantity())
        .subtotal(menuPrice*menuQuantity)
        .build();
  }

}
