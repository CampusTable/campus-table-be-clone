package shop.campustable.campustablebeclone.domain.order.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

  private final StringRedisTemplate stringRedisTemplate;

  @Async // 백그라운드에서 비동기로 실행하여 주문 처리 성능에 영향 최소화
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // 주문 트랜잭션 커밋 후 이벤트 처리
  public void handleOrderCreatedEvent(OrderCreatedEvent event) {
    try {
      event.getOrderItems().forEach(orderItem -> {
        log.info("리스너 데이터 확인: 주문 ID {}, 식당 ID {}, 메뉴 ID {}, 수량 {}",
            event.getOrderId(),
            event.getCafeteriaId(),
            orderItem.getMenu().getId(),
            orderItem.getQuantity()
        );
        String key = "cafeteria:" + event.getCafeteriaId() + ":menu:rank";
        stringRedisTemplate.opsForZSet().incrementScore(
            key,
            String.valueOf(orderItem.getMenu().getId()),
            orderItem.getQuantity()
        );
      });
      log.info("OrderEventListener: 주문 ID {}, 식당 ID {}", event.getOrderId(), event.getCafeteriaId());
    } catch (Exception e) {
      // Redis 업데이트 중 예외 발생 시 로그 기록 (실제 주문 처리에는 영향 없음)
      log.error("OrderEventListener: Redis 업데이트 중 에러 발생 (주문ID: {})", event.getOrderId(), e);
    }
  }

}
