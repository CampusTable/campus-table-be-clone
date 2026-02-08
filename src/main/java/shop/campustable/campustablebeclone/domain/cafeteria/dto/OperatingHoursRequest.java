  package shop.campustable.campustablebeclone.domain.cafeteria.dto;

  import com.fasterxml.jackson.annotation.JsonFormat;
  import io.swagger.v3.oas.annotations.media.Schema;
  import java.time.LocalTime;
  import lombok.AllArgsConstructor;
  import lombok.Builder;
  import lombok.Getter;
  import lombok.NoArgsConstructor;
  import shop.campustable.campustablebeclone.domain.cafeteria.entity.Cafeteria;
  import shop.campustable.campustablebeclone.domain.cafeteria.entity.DayOfWeekEnum;
  import shop.campustable.campustablebeclone.domain.cafeteria.entity.OperatingHours;

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public class OperatingHoursRequest {

    private DayOfWeekEnum dayOfWeek;

    @JsonFormat(pattern="HH:mm")
    private LocalTime openTime;

    @JsonFormat(pattern="HH:mm")
    private LocalTime closeTime;

    @JsonFormat(pattern="HH:mm")
    private LocalTime breaksStartTime;

    @JsonFormat(pattern="HH:mm")
    private LocalTime breaksCloseTime;

    public OperatingHours toEntity(Cafeteria cafeteria) {
      return OperatingHours.builder()
          .cafeteria(cafeteria)
          .dayOfWeek(this.dayOfWeek)
          .openTime(this.openTime)
          .closeTime(this.closeTime)
          .breaksStartTime(this.breaksStartTime)
          .breaksCloseTime(this.breaksCloseTime)
          .build();
    }

  }
