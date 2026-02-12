package shop.campustable.campustablebeclone.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.campustable.campustablebeclone.domain.order.entity.OrderItem;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {

  private String menuName;
  private int price;
  private int quantity;
  private int subtotal;

  public static OrderItemResponse from(OrderItem orderItem) {
    return OrderItemResponse.builder()
        .menuName(orderItem.getMenuName())
        .price(orderItem.getPrice())
        .quantity(orderItem.getQuantity())
        .subtotal(orderItem.getSubtotal())
        .build();
  }

}
