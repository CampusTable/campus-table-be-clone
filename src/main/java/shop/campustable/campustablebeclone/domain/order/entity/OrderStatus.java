package shop.campustable.campustablebeclone.domain.order.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

  PREPARING("조리 중"),   // 주문 생성 직후 ~ 조리 중인 상태
  READY("수령 대기"),       // 조리가 완료되어 학생을 기다리는 상태
  COMPLETED("조리 완료"),   // 학생이 음식을 가져가서 상황 종료
  CANCELLED("주문 취소");

  private final String description;

}
