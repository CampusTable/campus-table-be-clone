package shop.campustable.campustablebeclone.domain.order.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderSearchRequest {

  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private OrderStatus status;
  private Long cafeteriaId;

}
