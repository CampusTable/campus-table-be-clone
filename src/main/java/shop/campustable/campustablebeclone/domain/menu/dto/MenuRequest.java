package shop.campustable.campustablebeclone.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.campustable.campustablebeclone.domain.menu.entity.Menu;

@Getter
@NoArgsConstructor
public class MenuRequest {

  private String menuName;
  private Integer price;
  private Boolean available;
  private Integer stockQuantity;

  public Menu toEntity() {
    return Menu.builder()
        .menuName(menuName)
        .price(price)
        .menuUrl(null)
        .available(available)
        .stockQuantity(stockQuantity)
        .build();
  }

}
