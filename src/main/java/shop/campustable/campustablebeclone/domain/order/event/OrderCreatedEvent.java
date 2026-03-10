package shop.campustable.campustablebeclone.domain.order.event;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shop.campustable.campustablebeclone.domain.order.entity.OrderItem;

@Getter
@RequiredArgsConstructor
public class OrderCreatedEvent {

  private final Long cafeteriaId;
  private final List<OrderItem> orderItems;
  private final Long orderId;

}
