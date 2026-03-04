package shop.campustable.campustablebeclone.domain.order.dto;

import jakarta.validation.constraints.AssertTrue;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import shop.campustable.campustablebeclone.domain.order.entity.OrderStatus;

@Getter
@Setter
@NoArgsConstructor
public class OrderSearchRequest {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime startDate;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime endDate;
  private OrderStatus status;
  private Long cafeteriaId;

  @AssertTrue(message = "종료 날짜는 시작 날짜보다 이후여야 합니다.")
  public boolean isValidDateRange() {
    if (startDate == null || endDate == null) {
      return true; // 날짜가 null인 경우는 유효한 것으로 간주
    }
    return endDate.isAfter(startDate);
  }
}
