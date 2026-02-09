package shop.campustable.campustablebeclone.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.campustable.campustablebeclone.domain.category.entity.Category;
import shop.campustable.campustablebeclone.domain.menu.entity.Menu;

@Getter
@NoArgsConstructor
public class MenuRequest {

  private String menuName;
  private Integer price;
  private Boolean available;
  private Integer stockQuantity;

  public Menu toEntity(Category category) {

    Boolean isAvailable = this.stockQuantity != null
                          && this.stockQuantity > 0
                          && Boolean.TRUE.equals(this.available);

    return Menu.builder()
        .menuName(menuName)
        .category(category)
        .price(price)
        .menuUrl(null)
        .available(isAvailable)
        .stockQuantity(stockQuantity)
        .build();
  }

}
