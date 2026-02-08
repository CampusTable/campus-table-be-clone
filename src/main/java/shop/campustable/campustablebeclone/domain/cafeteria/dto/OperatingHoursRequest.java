  package shop.campustable.campustablebeclone.domain.cafeteria.dto;

  import com.fasterxml.jackson.annotation.JsonFormat;
  import io.swagger.v3.oas.annotations.media.Schema;
  import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private DayOfWeekEnum dayOfWeek;

    @NotNull
    @JsonFormat(pattern="HH:mm")
    private LocalTime openTime;

    @NotNull
    @JsonFormat(pattern="HH:mm")
    private LocalTime closeTime;

    @NotNull
    @JsonFormat(pattern="HH:mm")
    private LocalTime breaksStartTime;

    @NotNull
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
