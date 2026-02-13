package shop.campustable.campustablebeclone.domain.order.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.campustable.campustablebeclone.domain.order.entity.Order;
import shop.campustable.campustablebeclone.domain.order.entity.OrderItem;
import shop.campustable.campustablebeclone.domain.order.entity.OrderStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

  private Long orderId;
  private String cafeteriaName;
  private List<OrderItemResponse> orderItems;
  private int totalPrice;
  private OrderStatus status;
  private LocalDateTime orderTime;

  public static OrderResponse from(Order order){
    return OrderResponse.builder()
        .orderId(order.getId())
        .cafeteriaName(order.getCafeteria().getName())
        .orderItems(order.getOrderItems().stream()
            .map(OrderItemResponse::from)
            .toList())
        .totalPrice(order.getTotalPrice())
        .status(order.getStatus())
        .orderTime(order.getCreatedAt())
        .build();
  }

}