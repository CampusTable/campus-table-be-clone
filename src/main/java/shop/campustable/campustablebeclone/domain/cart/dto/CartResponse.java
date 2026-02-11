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

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CartItemResponse{
    private Long menuId;
    private String menuName;
    private String imageUrl;
    private int price;
    private int quantity;
  }

  public static CartResponse from(Cart cart, String s3Domain){
    List<CartItemResponse> cartItems = cart.getCartItems().stream()
        .map(cartItem->{

          String menuUrl = cartItem.getMenu().getMenuUrl();
          String fullUrl = null;

          if(menuUrl != null && !menuUrl.isBlank()){
            fullUrl = s3Domain + menuUrl;
          }

        })
        )
  }



}
