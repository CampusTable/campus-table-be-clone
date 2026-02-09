package shop.campustable.campustablebeclone.domain.cafeteria.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.campustable.campustablebeclone.domain.cafeteria.entity.Cafeteria;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CafeteriaRequest {

  private String name;
  private String description;
  private String address;

  public Cafeteria toEntity(){
    return Cafeteria.builder()
        .name(this.name)
        .description(this.description)
        .address(this.address)
        .build();
  }

}
