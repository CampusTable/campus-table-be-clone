package shop.campustable.campustablebeclone.domain.cafeteria.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.campustable.campustablebeclone.domain.cafeteria.entity.Cafeteria;
import shop.campustable.campustablebeclone.domain.cafeteria.entity.OperatingHours;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CafeteriaResponse {

  private Long id;
  private String name;
  private String description;
  private String address;
  private List<OperatingHoursResponse> operatingHours;

  public static CafeteriaResponse from(Cafeteria cafeteria) {

    return CafeteriaResponse.builder()
        .id(cafeteria.getId())
        .name(cafeteria.getName())
        .description(cafeteria.getDescription())
        .address(cafeteria.getAddress())
        .operatingHours(cafeteria.getOperatingHours().stream()
            .map(OperatingHoursResponse::from)
            .toList())
        .build();
  }

}
