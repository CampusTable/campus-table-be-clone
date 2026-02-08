package shop.campustable.campustablebeclone.domain.cafeteria.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperatingHours {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "operating_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cafeteria_id", nullable = false)
  private Cafeteria cafeteria;

  @Enumerated(EnumType.STRING)
  @Column(name = "day_of_week", nullable = false, length = 3)
  private DayOfWeekEnum dayOfWeek;

  @Column(name = "break_start_time")
  @JsonFormat(pattern="HH:mm")
  private LocalTime breaksStartTime;

  @Column(name = "break_close_time")
  @JsonFormat(pattern="HH:mm")
  private LocalTime breaksCloseTime;

}
