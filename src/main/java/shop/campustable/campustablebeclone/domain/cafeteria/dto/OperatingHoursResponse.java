package shop.campustable.campustablebeclone.domain.cafeteria.dto;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.campustable.campustablebeclone.domain.cafeteria.entity.DayOfWeekEnum;
import shop.campustable.campustablebeclone.domain.cafeteria.entity.OperatingHours;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OperatingHoursResponse {

  private Long id;
  private DayOfWeekEnum dayOfWeek;
  private LocalTime openTime;
  private LocalTime closeTime;
  private LocalTime breaksStartTime;
  private LocalTime breaksCloseTime;

  public static OperatingHoursResponse from(OperatingHours operatingHours) {
    return OperatingHoursResponse.builder()
        .id(operatingHours.getId())
        .dayOfWeek(operatingHours.getDayOfWeek())
        .openTime(operatingHours.getOpenTime())
        .closeTime(operatingHours.getCloseTime())
        .breaksStartTime(operatingHours.getBreaksStartTime())
        .breaksCloseTime(operatingHours.getBreaksCloseTime())
        .build();
  }

}
