package shop.campustable.campustablebeclone.domain.order.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

  PENDING("주문 대기"),
  ACCEPTED("주문 수락"),
  COOKING("조리 중"),
  COMPLETED("조리 완료"),
  CANCELLED("주문 취소");

  private final String description;

}
