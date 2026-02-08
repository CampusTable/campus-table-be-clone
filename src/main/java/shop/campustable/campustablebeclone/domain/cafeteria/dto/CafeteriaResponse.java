package shop.campustable.campustablebeclone.domain.cafeteria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.campustable.campustablebeclone.domain.cafeteria.entity.Cafeteria;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CafeteriaResponse {

  private Long id;
  private String name;
  private String description;
  private String address;

  public static CafeteriaResponse from(Cafeteria cafeteria) {

    return CafeteriaResponse.builder()
        .id(cafeteria.getId())
        .name(cafeteria.getName())
        .description(cafeteria.getDescription())
        .address(cafeteria.getAddress())
        .build();
  }

}
