package shop.campustable.campustablebeclone.domain.cart.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.campustable.campustablebeclone.domain.cart.entity.Cart;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {

  private Long cartId;
  private Long cafeteriaId;
  private List<CartItemResponse> cartItems;
  private int totalPrice;


  public static CartResponse from(Cart cart,List<CartItemResponse> cartItems,int totalPrice) {
    return CartResponse.builder()
        .cartId(cart.getId())
        .cafeteriaId(cart.getCafeteriaId())
        .cartItems(cartItems)
        .totalPrice(totalPrice)
        .build();
  }

  public static CartResponse empty(){
    return CartResponse.builder()
        .cartId(null)
        .cafeteriaId(null)
        .cartItems(List.of())
        .totalPrice(0)
        .build();
  }



}
